package ch.bfh.ti.gapa.cli.printer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class GapaInfoPrinterImplTest {

    /**
     * Tests, if the json schema can be loaded and printed.
     * @throws IOException if schema cannot be loaded.
     */
    @Test
    void printConfigSchema() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);
        GapaInfoPrinterImpl gapaInfoPrinter = new GapaInfoPrinterImpl(pw);
        gapaInfoPrinter.printConfigSchema();
        pw.flush();

        Assertions.assertTrue(out.toString("utf8").length() > 0, "There is output");
    }
}