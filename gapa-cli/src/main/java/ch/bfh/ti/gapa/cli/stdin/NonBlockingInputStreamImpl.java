package ch.bfh.ti.gapa.cli.stdin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * This implementation allows to read asynchronously/non-blocking from an {@link InputStream}.
 * A new thread is started that checks every 20ms if a new line
 * is ready to read. The read line is passed to {@link NonBlockingInputStreamHandler#onReadLine(String)}.
 * The thread closes gracefully if {@link #close()} is called.
 * Note that the implementation of the InputStream has to overwrite the {@link InputStream#available()} method.
 *
 * This class exists because a blocking {@link InputStream#read()} call never stops blocking
 * if there is never input. This class allows to stop reading from an input stream even if there is no input.
 */
public class NonBlockingInputStreamImpl implements NonBlockingInputStream {
    private Thread thread;

    public void start(InputStream in, NonBlockingInputStreamHandler nonBlockingInputStreamHandler) {
        thread = new Thread(() -> {
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("utf8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                try {
                    if (bufferedReader.ready()) {
                        String line = bufferedReader.readLine();
                        nonBlockingInputStreamHandler.onReadLine(line);
                    } else {
                        Thread.sleep(20);
                    }
                } catch(InterruptedException e) {
                    //terminate thread normally
                    break;
                } catch (Throwable t) {
                    //pass exception to handler
                    nonBlockingInputStreamHandler.onException(t);
                    //no further attempts to read from the stream will be made
                    break;
                }
            }
        });
        thread.start();
    }

    /**
     * Stops reading from {@link InputStream} and leads to termination of extra thread.
     */
    public void close() {
       thread.interrupt();
    }
}
