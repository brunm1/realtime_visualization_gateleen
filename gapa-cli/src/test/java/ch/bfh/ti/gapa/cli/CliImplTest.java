package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.CliConfigOptions;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.config.parsing.CliInputParser;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.exception.CommandLineException;
import ch.bfh.ti.gapa.cli.printer.CliPrintOptions;
import ch.bfh.ti.gapa.cli.printer.InfoPrinter;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStream;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStreamHandler;
import ch.bfh.ti.gapa.process.AsyncTaskHandler;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Tests the CLI implementation. Every exit code is verified.
 * Dependencies are mocked.
 */
class CliImplTest {
    private CliImpl cliImpl;

    @BeforeEach
    void beforeEach() {
        //Mock every dependency
        ProcessLayer processLayerMock = new ProcessLayer() {
            @Override
            public void stopRecording() {
            }

            @Override
            public void run(ProcessLayerInput processLayerInput, AsyncTaskHandler<String> asyncHandler) {
            }
        };
        ConfigFileReader configFileReader = new ConfigFileReader() {
            @Override
            public void readConfigFile(CliInput cliInput) {
            }

            @Override
            public void readConfigFile(CliInput cliInput, Path configFilePath) {
            }
        };
        CommandLineArgumentsReader commandLineArgumentsReaderMock = (input, commandLine) -> {};
        CliInputParser cliInputParserMock = (rawInput, input) -> {};
        CliConfigOptions cliConfigOptions = new CliConfigOptions();
        CliPrintOptions cliPrintOptions = new CliPrintOptions();
        InfoPrinter infoPrinter = new InfoPrinter() {
            @Override
            public void printHelp() {
            }

            @Override
            public void printVersion() {
            }

            @Override
            public void printCommandLineException(CommandLineException e) {
            }

//            @Override
//            public void printConfigSchema() {
//            }
        };
        NonBlockingInputStream nonBlockingInputStream = new NonBlockingInputStream() {
            @Override
            public void start(InputStream in, NonBlockingInputStreamHandler nonBlockingStdInHandler) {
            }

            @Override
            public void close() {
            }
        };
        Consumer<String> printer = s->{};

//        cliImpl = new CliImpl(
//                processLayerMock,
//                configFileReader,
//                commandLineArgumentsReaderMock,
//                cliInputParserMock,
//                infoPrinter,
//                cliConfigOptions,
//                nonBlockingInputStream,
//                printer,
//                cliPrintOptions);
    }

//    @Test
//    void testHelpOutput() throws IOException {
//        //prepare behaviour of mocked dependencies
//        cliImpl.setPrinter();
//
//        //Run with
//        int exitCode = cliImpl.run(new String[]{"-h"});
//
//        String helpOutput = ResourceReader.readStringFromResource("/expectedHelpOutput.txt");
//        Assertions.assertEquals(helpOutput, stdoutByteArrayOutputStream.toString());
//        Assertions.assertEquals(0, exitCode);
//    }
//
//    private void checkException(CommandLineExceptionType commandLineExceptionType, int actualExitCode, String firstLineStackTrace) {
//        String[] lines = stderrByteArrayOutputStream.toString().split("\n");
//        String firstTwoLines = lines[0] + "\n" + lines[1];
//
//        Assertions.assertEquals(commandLineExceptionType.getDesc()+"\n" +
//                        firstLineStackTrace,
//                firstTwoLines);
//        Assertions.assertEquals(commandLineExceptionType.getCode(), actualExitCode);
//    }
//
//    @Test
//    void testNormalExecution() {
//        //Run with
//        int exitCode = cliImpl.run(new String[]{});
//
//        Assertions.assertEquals("\n", stdoutByteArrayOutputStream.toString());
//        Assertions.assertEquals(0, exitCode);
//    }

//    @Test
//    void testINVALID_COMMAND_USAGE() {
//        //Run with
//        int exitCode = cliImpl.run(new String[]{"--invalid"});
//
//        checkException(CommandLineExceptionType.INVALID_COMMAND_USAGE, exitCode,
//                "org.apache.commons.cli.UnrecognizedOptionException: Unrecognized option: --invalid");
//    }
//
//    @Test
//    void testUNRECOGNIZED_ARGUMENTS() {
//        //Run with
//        int exitCode = cliImpl.run(new String[]{"what", "is", "this", "-w", "sdofje"});
//
//        checkException(CommandLineExceptionType.UNRECOGNIZED_ARGUMENTS, exitCode,
//                "java.lang.Exception: Unrecognized arguments: what, is, this");
//    }

//    @Test
//    void testWebsocketOption() {
//        //Run with
//        int exitCode = cliImpl.run(new String[]{
//                "-w",
//                "http://localhost:7012"
//        });
//
//        Assertions.assertEquals(0, exitCode);
//    }
}