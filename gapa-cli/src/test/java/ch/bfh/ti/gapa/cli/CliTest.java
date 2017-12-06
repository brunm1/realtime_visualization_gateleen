package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.reading.file.DefaultConfigFileReader;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.*;

import java.io.*;

/**
If test fails, fix it and correct also documentation
 */
class CliTest {
    private static ByteArrayOutputStream byteArrayOutputStream;
    private ProcessLayer processLayerMock = input -> null;
    private DefaultConfigFileReader defaultConfigFileReader = input -> {};
    private CommandLineArgumentsReader commandLineArgumentsReader = (input, commandLine) -> {};
    private RawInputParser rawInputParser = (rawInput, input) -> {};

    @BeforeAll
    static void beforeAll() {
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
    @Disabled
        //TODO
    void testHelpOutput() throws IOException {
        //Run with
        int exitCode = new Cli(processLayerMock, defaultConfigFileReader, commandLineArgumentsReader, rawInputParser).run(new String[]{"-h"});

        String helpOutput = ResourceReader.readStringFromResource("/expectedHelpOutput.txt");
        Assertions.assertEquals(helpOutput, byteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testNormalExecution() {
        ProcessLayer processLayerMock = input -> "plantuml";

        //Run with
        int exitCode = new Cli(processLayerMock, defaultConfigFileReader, commandLineArgumentsReader, rawInputParser).run(new String[]{});

        Assertions.assertEquals("plantuml", byteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testINVALID_COMMAND_USAGE() {
        //Run with
        int exitCode = new Cli(processLayerMock, defaultConfigFileReader, commandLineArgumentsReader, rawInputParser).run(new String[]{"--invalid"});

        Assertions.assertEquals("Invalid command usage. Cause: Unrecognized option: --invalid\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(5, exitCode);
    }

    @Test
    void testUNRECOGNIZED_ARGUMENTS() {
        //Run with
        int exitCode = new Cli(processLayerMock, defaultConfigFileReader, commandLineArgumentsReader, rawInputParser).run(new String[]{"what", "is", "this", "-f", "sdofje"});

        Assertions.assertEquals("Could not recognize some arguments. Cause: what, is, this\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(6, exitCode);
    }

    @Test
    void testMostOptions() {
        //mock input stream
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{});
        System.setIn(byteArrayInputStream);

        //Run with
        int exitCode = new Cli(processLayerMock, defaultConfigFileReader, commandLineArgumentsReader, rawInputParser).run(new String[]{
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