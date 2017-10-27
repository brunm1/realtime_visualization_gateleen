package ch.bfh.ti.gapa.process.resources;

import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ResourceReader {
    public static String readStringFromResource(String resourceName) throws IOException {
        InputStream is = ResourceReader.class.getResourceAsStream(resourceName);
        return StringFromInputStreamReader.readStringFromInputStream(is, Charset.forName("UTF-8"), 1024);
    }
}
