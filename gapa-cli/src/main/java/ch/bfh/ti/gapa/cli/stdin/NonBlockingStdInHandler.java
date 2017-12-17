package ch.bfh.ti.gapa.cli.stdin;

public interface NonBlockingStdInHandler {
    void onReadLine(String line);
}
