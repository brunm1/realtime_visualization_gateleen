package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.file.DefaultConfigFileReader;
import ch.bfh.ti.gapa.cli.printer.InfoPrinter;
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
    private DefaultConfigFileReader defaultConfigFileReaderMock = input -> {};
    private CommandLineArgumentsReader commandLineArgumentsReaderMock = (input, commandLine) -> {};
    private RawInputParser rawInputParserMock = (rawInput, input) -> {};
    private CliOptions cliOptions = new CliOptions();
    private InfoPrinter infoPrinter = new InfoPrinter(cliOptions);
    private Cli cli = new Cli(
            processLayerMock,
            defaultConfigFileReaderMock,
            commandLineArgumentsReaderMock,
            rawInputParserMock,
            infoPrinter,
            cliOptions
    );

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
        int exitCode = cli.run(new String[]{"-h"});

        String helpOutput = ResourceReader.readStringFromResource("/expectedHelpOutput.txt");
        Assertions.assertEquals(helpOutput, byteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testNormalExecution() {
        //Run with
        int exitCode = cli.run(new String[]{});

        Assertions.assertEquals("plantuml", byteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testINVALID_COMMAND_USAGE() {
        //Run with
        int exitCode = cli.run(new String[]{"--invalid"});

        Assertions.assertEquals("Invalid command usage. Cause: Unrecognized option: --invalid\n",
                byteArrayOutputStream.toString());
        Assertions.assertEquals(5, exitCode);
    }

    @Test
    void testUNRECOGNIZED_ARGUMENTS() {
        //Run with
        int exitCode = cli.run(new String[]{"what", "is", "this", "-f", "sdofje"});

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
        int exitCode = cli.run(new String[]{
                "-w",
                "http://localhost:7012"
        });

        Assertions.assertEquals(0, exitCode);
    }
}