package ch.bfh.ti.gapa.cli.stdin;

/**
 * Handler interface used by {@link NonBlockingInputStream}.
 */
public interface NonBlockingInputStreamHandler {
    /**
     * @param line that was read
     */
    void onReadLine(String line) throws Throwable;

    void onException(Throwable t);
}
