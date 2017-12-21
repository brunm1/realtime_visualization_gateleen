package ch.bfh.ti.gapa.cli.stdin;

import java.io.InputStream;

/**
 * Calls the {@link NonBlockingInputStreamHandler#onReadLine(String)} method of the
 * given handler if there is a new line read from StdIn.
 */
public interface NonBlockingInputStream {
    void start(InputStream in, NonBlockingInputStreamHandler nonBlockingInputStreamHandler);
    void close();
}
