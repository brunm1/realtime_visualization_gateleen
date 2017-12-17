package ch.bfh.ti.gapa.cli.stdin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    };

    public void close() {
       closed=true;
    }
}
