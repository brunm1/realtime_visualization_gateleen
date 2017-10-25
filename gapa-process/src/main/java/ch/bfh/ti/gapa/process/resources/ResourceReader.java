package ch.bfh.ti.gapa.process.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ResourceReader {
    public static String readStringFromResource(String resourceName) throws IOException {
        InputStream is = ResourceReader.class.getResourceAsStream(resourceName);
        char[] buf = new char[1024];
        Reader r = new InputStreamReader(is, "UTF-8");
        StringBuilder s = new StringBuilder();
        int n;
        while((n = r.read(buf)) >= 0)
            s.append(buf, 0, n);
        return s.toString();
    }
}
