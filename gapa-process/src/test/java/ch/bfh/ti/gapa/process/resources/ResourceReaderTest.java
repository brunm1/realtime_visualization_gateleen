package ch.bfh.ti.gapa.process.resources;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceReaderTest {
    @Test
    void readStringFromResource() throws IOException {
        assertEquals("dummy", ResourceReader.readStringFromResource("/mock"));
    }

}