package ch.bfh.ti.gapa.cli.stdin;

/**
 * Calls the {@link NonBlockingStdInHandler#onReadLine(String)} method of the
 * given handler if there is a new line read from StdIn.
 */
public interface NonBlockingStdIn {
    void start(NonBlockingStdInHandler nonBlockingStdInHandler);
    void close();
}
