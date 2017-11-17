package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class CliTest {

    @Test
    void testHelpOutput() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //If test fails, fix it and correct also documentation
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);

        //Run with
        new Cli(processLayerMock).run(new String[]{"-h"});


        String helpOutput = ResourceReader.readStringFromResource("/expectedHelpOutput.txt");
        Assertions.assertEquals(helpOutput, byteArrayOutputStream.toString());
    }

    @Test
    void testNormalExecution() throws IOException {
        ProcessLayer processLayerMock = input -> "plantuml";

        //If test fails, fix it and correct also documentation
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);

        //Run with
        new Cli(processLayerMock).run(new String[]{});

        Assertions.assertEquals("plantuml", byteArrayOutputStream.toString());
    }
}