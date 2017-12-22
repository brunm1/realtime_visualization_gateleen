package ch.bfh.ti.gapa.process;

public interface AsyncTask<T, K> {
    void run(K input, AsyncTaskHandler<T> asyncHandler) throws Throwable;
}
