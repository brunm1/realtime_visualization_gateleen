package ch.bfh.ti.gapa.cli.stdin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This implementation allows to read asynchronously from StdIn.
 * A new thread is started that checks every 20ms if a new line
 * is ready to read. The read line is passed to {@link NonBlockingStdInHandler#onReadLine(String)}.
 * The thread closes gracefully if {@link #close()} is called.
 */
public class NonBlockingStdInImpl implements NonBlockingStdIn {
    private volatile boolean closed;

    public void start(NonBlockingStdInHandler nonBlockingStdInHandler) {
        closed=false;
        new Thread(() -> {
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                if(closed) {
                    break;
                } else {
                    try {
                        if (bufferedReader.ready()) {
                            String line = bufferedReader.readLine();
                            nonBlockingStdInHandler.onReadLine(line);
                        } else {
                            Thread.sleep(20);
                        }
                    } catch (IOException | InterruptedException e1) {
                        e1.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    /**
     * Stops reading from StdIn and shuts down extra thread.
     */
    public void close() {
       closed=true;
    }
}
