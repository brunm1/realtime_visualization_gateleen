package ch.bfh.ti.gapa.process.reader;

import java.io.*;

public class StreamRedirection {
    public static void redirectStream(InputStream is, OutputStream os, int buffSize) throws IOException {
        byte[] buf = new byte[buffSize];
        int n;
        while((n = is.read(buf)) >= 0)
            os.write(buf, 0, n);
        is.close();
        os.close();
    }
}
