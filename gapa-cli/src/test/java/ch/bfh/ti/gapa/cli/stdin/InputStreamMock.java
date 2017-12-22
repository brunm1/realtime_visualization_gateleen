package ch.bfh.ti.gapa.cli.stdin;

import java.io.InputStream;

/**
 * A valid InputStream implementation for {@link NonBlockingInputStream}.
 */
public class InputStreamMock extends InputStream{
    private byte[] bytes;

    private int i = 0;
    private boolean available = false;

    InputStreamMock(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int read() {
        return i<bytes.length ? bytes[i++] : -1;
    }

    //Overwriting this method is crucial. It allows to check
    //if bytes can be read without blocking the thread.
    @Override
    public int available() {
        //simulate that no bytes are available at first
        if(!available) {
            available=true;
            return 0;
        }
        return i<bytes.length ? bytes.length-i : 0;
    }
}
