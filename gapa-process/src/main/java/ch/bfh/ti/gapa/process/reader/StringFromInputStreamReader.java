package ch.bfh.ti.gapa.process.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class StringFromInputStreamReader {
    public static String readStringFromInputStream(InputStream is, Charset charset, int buffSize) throws IOException {
        char[] buf = new char[buffSize];
        Reader r = new InputStreamReader(is, charset);
        StringBuilder s = new StringBuilder();
        int n;
        while((n = r.read(buf)) >= 0)
            s.append(buf, 0, n);
        is.close();
        return s.toString();
    }
}
