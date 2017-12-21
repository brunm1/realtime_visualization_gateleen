package ch.bfh.ti.gapa.cli.stdin;

/**
 * Handler interface used by {@link NonBlockingStdIn}.
 */
public interface NonBlockingStdInHandler {
    /**
     * @param line that was read
     */
    void onReadLine(String line);
}
