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
        //Restore stdout and stdin
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setIn(new FileInputStream(FileDescriptor.in));
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

    @Test
    void testFAILED_PARSING_INBOUND_REQUEST_PATTERN() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"-i", "i]n)valid[ regex patt(ern"});

        Assertions.assertEquals("Could not parse inbound request pattern. Cause: Unmatched closing ')' near index 2\n" +
                        "i]n)valid[ regex patt(ern\n" +
                        "  ^\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(2, exitCode);
    }

    @Test
    void testFAILED_PARSING_OUTBOUND_REQUEST_PATTERN() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"-o", "i]n)valid[ regex patt(ern"});

        Assertions.assertEquals("Could not parse outbound request pattern. Cause: Unmatched closing ')' near index 2\n" +
                        "i]n)valid[ regex patt(ern\n" +
                        "  ^\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(3, exitCode);
    }

    @Test
    void testFAILED_PARSING_DATE_TIME_PATTERN() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"-t", "i]n)valid[ date time patt(ern"});

        Assertions.assertEquals("Could not parse date time pattern. Cause: Unknown pattern letter: i\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(4, exitCode);
    }

    @Test
    void testINVALID_COMMAND_USAGE() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"--invalid"});

        Assertions.assertEquals("Invalid command usage. Cause: Unrecognized option: --invalid\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(5, exitCode);
    }

    @Test
    void testUNRECOGNIZED_ARGUMENTS() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{"what", "is", "this", "-f", "sdofje"});

        Assertions.assertEquals("Could not recognize some arguments. Cause: what, is, this\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(6, exitCode);
    }

    @Test
    void testMostOptions() throws IOException {
        ProcessLayer processLayerMock = input -> null;

        //mock input stream
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{});
        System.setIn(byteArrayInputStream);

        //Run with
        int exitCode = new Cli(processLayerMock).run(new String[]{
                "-i",
                "\"(<?date>\\S+) " +
                "sender=(<?sender\\S+) " +
                "(<?method>\\S+) (\\S+) " +
                "(<?url>\\S+)\"",
                "-o",
                "\"(<?date>\\S+) " +
                "receiver=(<?sender\\S+) " +
                "(<?method>\\S+) (\\S+) " +
                "(<?url>\\S+)\"",
                "-t",
                "yyyy-MM-dd'T'hh:mm:ss",
        });

        Assertions.assertEquals(0, exitCode);
    }
}