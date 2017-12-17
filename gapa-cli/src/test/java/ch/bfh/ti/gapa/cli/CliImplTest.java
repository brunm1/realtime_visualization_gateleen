package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.parsing.CliInputParser;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.exception.CommandLineExceptionType;
import ch.bfh.ti.gapa.cli.printer.InfoPrinter;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingStdIn;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingStdInHandler;
import ch.bfh.ti.gapa.process.AsyncTaskHandler;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Path;

/**
If test fails, fix it and correct also documentation
 */
class CliImplTest {
    private static ByteArrayOutputStream stdoutByteArrayOutputStream;
    private static ByteArrayOutputStream stderrByteArrayOutputStream;
    private ProcessLayer processLayerMock = new ProcessLayer() {
        @Override
        public void stopRecording() {

        }

        @Override
        public void run(ProcessLayerInput processLayerInput, AsyncTaskHandler<String> asyncHandler) {
            asyncHandler.onResult("");
        }
    };
    private ConfigFileReader configFileReader = new ConfigFileReader() {
        @Override
        public void readConfigFile(CliInput cliInput) {

        }

        @Override
        public void readConfigFile(CliInput cliInput, Path configFilePath) {

        }
    };
    private CommandLineArgumentsReader commandLineArgumentsReaderMock = (input, commandLine) -> {};
    private CliInputParser cliInputParserMock = (rawInput, input) -> {};
    private CliOptions cliOptions = new CliOptions();
    private InfoPrinter infoPrinter = new InfoPrinter(cliOptions);
    private NonBlockingStdIn nonBlockingStdIn = new NonBlockingStdIn() {
        @Override
        public void start(NonBlockingStdInHandler nonBlockingStdInHandler) {
            nonBlockingStdInHandler.onReadLine("");
        }

        @Override
        public void close() {

        }
    };
    private CliImpl cliImpl = new CliImpl(
            processLayerMock,
            configFileReader,
            commandLineArgumentsReaderMock,
            cliInputParserMock,
            infoPrinter,
            cliOptions,
            nonBlockingStdIn
    );

    @BeforeAll
    static void beforeAll() {
        //mock output stream
        stdoutByteArrayOutputStream = new ByteArrayOutputStream();
        stderrByteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdoutByteArrayOutputStream));
        System.setErr(new PrintStream(stderrByteArrayOutputStream));
    }

    @BeforeEach
    void beforeEach() {
        stdoutByteArrayOutputStream.reset();
        stderrByteArrayOutputStream.reset();
    }

    @AfterAll
    static void afterAll() {
        //Restore stdout and stdin
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
    }

    @Test
    void testHelpOutput() throws IOException {
        //Run with
        int exitCode = cliImpl.run(new String[]{"-h"});

        String helpOutput = ResourceReader.readStringFromResource("/expectedHelpOutput.txt");
        Assertions.assertEquals(helpOutput, stdoutByteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    private void checkException(CommandLineExceptionType commandLineExceptionType, int actualExitCode, String firstLineStackTrace) {
        String[] lines = stderrByteArrayOutputStream.toString().split("\n");
        String firstTwoLines = lines[0] + "\n" + lines[1];

        Assertions.assertEquals(commandLineExceptionType.getDesc()+"\n" +
                        firstLineStackTrace,
                firstTwoLines);
        Assertions.assertEquals(commandLineExceptionType.getCode(), actualExitCode);
    }

    @Test
    void testNormalExecution() {
        //Run with
        int exitCode = cliImpl.run(new String[]{});

        Assertions.assertEquals("\n", stdoutByteArrayOutputStream.toString());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testINVALID_COMMAND_USAGE() {
        //Run with
        int exitCode = cliImpl.run(new String[]{"--invalid"});

        checkException(CommandLineExceptionType.INVALID_COMMAND_USAGE, exitCode,
                "org.apache.commons.cli.UnrecognizedOptionException: Unrecognized option: --invalid");
    }

    @Test
    void testUNRECOGNIZED_ARGUMENTS() {
        //Run with
        int exitCode = cliImpl.run(new String[]{"what", "is", "this", "-w", "sdofje"});

        checkException(CommandLineExceptionType.UNRECOGNIZED_ARGUMENTS, exitCode,
                "java.lang.Exception: Unrecognized arguments: what, is, this");
    }

    @Test
    void testWebsocketOption() {
        //Run with
        int exitCode = cliImpl.run(new String[]{
                "-w",
                "http://localhost:7012"
        });

        Assertions.assertEquals(0, exitCode);
    }
}