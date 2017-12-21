package ch.bfh.ti.gapa.process;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class SynchronizedTask<T, K> {
    private AsyncTask<T, K> asyncTask;
    private Thread creatingThread;

    private CountDownLatch countDownLatch;
    private AtomicReference<Throwable> errorRef = new AtomicReference<>();
    private AtomicReference<T> resultRef = new AtomicReference<>();

    public SynchronizedTask(AsyncTask<T, K> asyncTask) {
        this.asyncTask = asyncTask;
        creatingThread = Thread.currentThread();
    }

    public void outsideExceptionInterrupts(Throwable t) {
        errorRef.set(t);
        creatingThread.interrupt();
    }


    private T waitForResult() throws Throwable{
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //occurs when outsideExceptionInterrupts was called.
            //We don't wait for the result from the other thread and
            //throw immediately.
        }

        //throw ex if there was one
        //TODO possible race condition: errorRef can be overwritten from different thread after this thread
        //was interrupted with outsideExceptionInterrupts
        if(errorRef.get() != null) {
            errorRef.get().printStackTrace();
            throw errorRef.get();
        } else {
            return resultRef.get();
        }
    }

    private void runAsyncTask(K input) {
        asyncTask.run(input, new AsyncTaskHandler<T>() {
            @Override
            public void onResult(T result) {
                resultRef.set(result);
                countDownLatch.countDown();
            }

            @Override
            public void onError(Throwable err) {
                errorRef.set(err);
                countDownLatch.countDown();
            }
        });
    }

    private void initialize() {
        countDownLatch = new CountDownLatch(1);
        errorRef = new AtomicReference<>();
        resultRef = new AtomicReference<>();
    }

    public T run(K input) throws Throwable {
        initialize();

        runAsyncTask(input);

        return waitForResult();
    }

    public T run(K input, Runnable runAfter) throws Throwable {
        initialize();
        runAsyncTask(input);
        runAfter.run();
        return waitForResult();
    }


}
