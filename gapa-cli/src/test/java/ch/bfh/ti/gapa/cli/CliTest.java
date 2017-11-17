package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.*;

import java.io.*;

/**
If test fails, fix it and correct also documentation
 */
class CliTest {
    private static ByteArrayOutputStream byteArrayOutputStream;


    @BeforeAll
    public static void beforeAll() {
        //mock output stream
        byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);
    }

    @BeforeEach
    void beforeEach() {
        byteArrayOutputStream.reset();
    }

    @AfterAll
    static void afterAll() {
        //Restore stdout
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    @Test
    void testHelpOutput() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"-h"});

        String helpOutput = ResourceReader.readStringFromResource("/expectedHelpOutput.txt");
        Assertions.assertEquals(helpOutput, byteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testNormalExecution() throws IOException {
        ProcessLayer processLayerMock = input -> "plantuml";

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{});

        Assertions.assertEquals("plantuml", byteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testFILE_NOT_FOUND() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"-f", "non_existing_file"});

        Assertions.assertEquals("Could not find file. Cause: non_existing_file (No such file or directory)\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(1, exitCode);
    }
}