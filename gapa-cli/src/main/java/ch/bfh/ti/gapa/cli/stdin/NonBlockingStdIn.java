package ch.bfh.ti.gapa.cli.stdin;
public interface NonBlockingStdIn {
    void start(NonBlockingStdInHandler nonBlockingStdInHandler);
    void close();
}
