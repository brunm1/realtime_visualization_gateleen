package ch.bfh.ti.gapa.cli.stdin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

class NonBlockingInputStreamImplTest {

    @Test
    void start() throws InterruptedException, UnsupportedEncodingException {
        byte[] bytes = "こんにしは".getBytes("utf8");
        NonBlockingInputStream nonBlockingInputStream = new NonBlockingInputStreamImpl();

        AtomicReference<String> input = new AtomicReference<>();
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        nonBlockingInputStream.start(new InputStreamMock(bytes), new NonBlockingInputStreamHandler() {
            @Override
            public void onReadLine(String line) {
                input.set(line);

                //terminates further reading
                nonBlockingInputStream.close();

                countDownLatch.countDown();
            }

            @Override
            public void onException(Throwable t) {
                throwable.set(t);
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();

        Assertions.assertNull(throwable.get());
        Assertions.assertArrayEquals(bytes, input.get().getBytes(Charset.forName("utf8")));
    }

    @Test
    void startIOException() throws InterruptedException {
        NonBlockingInputStream nonBlockingInputStream = new NonBlockingInputStreamImpl();

        AtomicReference<Throwable> throwable = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        RuntimeException e = new RuntimeException(
            "Sum Ting Wong\n" +
            "Wi Tu Lo\n"+
            "Ho Lee Fuk\n"+
            "Bang Ding Ow"
        );

        InputStream in = new InputStream() {
            @Override
            public int read() {
                throw e;
            }

            @Override
            public int available() {
                throw e;
            }
        };

        nonBlockingInputStream.start(in, new NonBlockingInputStreamHandler() {
            @Override
            public void onReadLine(String line) {
            }

            @Override
            public void onException(Throwable t) {
                throwable.set(t);
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        Assertions.assertEquals(e, throwable.get());
    }
}