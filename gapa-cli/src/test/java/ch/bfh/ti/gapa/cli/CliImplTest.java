package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.CliConfigOptions;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.config.parsing.CliInputParser;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.exception.CommandLineException;
import ch.bfh.ti.gapa.cli.exception.GapaCommandLineExceptionType;
import ch.bfh.ti.gapa.cli.printer.CliPrintOptions;
import ch.bfh.ti.gapa.cli.printer.GapaInfoPrinter;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStream;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStreamHandler;
import ch.bfh.ti.gapa.process.AsyncTaskHandler;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
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
                asyncHandler.onResult("result");
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);
        GapaInfoPrinter infoPrinter = new GapaInfoPrinter() {
            @Override
            public void printHelp() {
            }

            @Override
            public void printVersion() throws IOException {

            }

            @Override
            public void printCommandLineException(CommandLineException commandLineException) {

            }

            @Override
            public void printConfigSchema() throws IOException {

            }
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

        cliImpl = new CliImpl(
                processLayerMock,
                configFileReader,
                commandLineArgumentsReaderMock,
                cliInputParserMock,
                infoPrinter,
                cliConfigOptions,
                nonBlockingInputStream,
                printer,
                cliPrintOptions);
    }

    @Test
    void testHelpOption() {
        AtomicReference<Boolean> printHelpCalled = new AtomicReference<>(false);

        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printHelp() {
                printHelpCalled.set(true);
            }

            @Override
            public void printVersion() {}
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {}
            @Override
            public void printConfigSchema() {}
        });

        //Run with
        int exitCode = cliImpl.run(new String[]{"-h"});

        Assertions.assertTrue(printHelpCalled.get());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testVersionOption(){
        AtomicReference<Boolean> called = new AtomicReference<>(false);

        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printHelp() {}

            @Override
            public void printVersion() {
                called.set(true);
            }
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {}
            @Override
            public void printConfigSchema() {}
        });

        //Run with
        int exitCode = cliImpl.run(new String[]{"-v"});

        Assertions.assertTrue(called.get());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testSchemaOption() {
        AtomicReference<Boolean> called = new AtomicReference<>(false);

        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printHelp() {}
            @Override
            public void printVersion() {}
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {}
            @Override
            public void printConfigSchema() {
                called.set(true);
            }
        });

        //Run with
        int exitCode = cliImpl.run(new String[]{"-s"});

        Assertions.assertTrue(called.get());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testInvalidPrintOption() {
        AtomicReference<Boolean> printHelpCalled = new AtomicReference<>(false);
        AtomicReference<Boolean> printCommandLineExceptionCalled = new AtomicReference<>(false);

        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printHelp() {
                printHelpCalled.set(true);
            }
            @Override
            public void printVersion() {}
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {
                printCommandLineExceptionCalled.set(true);
            }
            @Override
            public void printConfigSchema() {
            }
        });

        //Run with
        int exitCode = cliImpl.run(new String[]{"-h", "-c"});

        Assertions.assertFalse(printHelpCalled.get());
        Assertions.assertTrue(printCommandLineExceptionCalled.get());
        Assertions.assertEquals(GapaCommandLineExceptionType.UNRECOGNIZED_ARGUMENTS.getCode(), exitCode);
    }

    @Test
    void testConfigOptions() {
        String pathToConfigFile = "path/to/config/file";
        AtomicReference<Boolean> readConfigFileCalled = new AtomicReference<>(false);
        AtomicReference<Boolean> readCalled = new AtomicReference<>(false);

        cliImpl.setConfigFileReader(new ConfigFileReader() {
            @Override
            public void readConfigFile(CliInput cliInput) {

            }

            @Override
            public void readConfigFile(CliInput cliInput, Path configFilePath) {
                readConfigFileCalled.set(true);
                Assertions.assertEquals(pathToConfigFile, configFilePath.toString());
            }
        });

        cliImpl.setCommandLineArgumentsReader(new CommandLineArgumentsReader() {
            @Override
            public void read(CliInput cliInput, CommandLine commandLine) {
                readCalled.set(true);
                Assertions.assertTrue(commandLine.hasOption("n"));
                Assertions.assertTrue(commandLine.hasOption("w"));
            }
        });

        //Run with
        int exitCode = cliImpl.run(new String[]{"-c",pathToConfigFile,"-n","serverName","-w","ws://localhost:8888"});

        Assertions.assertTrue(readConfigFileCalled.get());
        Assertions.assertTrue(readCalled.get());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void testInvalidConfigOption() {
        AtomicReference<Boolean> printCommandLineExceptionCalled = new AtomicReference<>(false);


        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printHelp() {}
            @Override
            public void printVersion() {}
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {
                printCommandLineExceptionCalled.set(true);
                Assertions.assertEquals("-a",((UnrecognizedOptionException)commandLineException.getThrowable()).getOption());
            }
            @Override
            public void printConfigSchema() {
            }
        });

        //Run with
        int exitCode = cliImpl.run(new String[]{"-n", "asdf", "-a"});

        Assertions.assertTrue(printCommandLineExceptionCalled.get());
        Assertions.assertEquals(GapaCommandLineExceptionType.INVALID_COMMAND_USAGE.getCode(), exitCode);
    }

    @Test
    void testNormalExecution() {
        AtomicReference<String> str = new AtomicReference<>("");
        Consumer<String> printer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                str.set(str.get()+s);
            }
        };
        cliImpl.setPrinter(printer);
        //Run with
        int exitCode = cliImpl.run(new String[]{});

        Assertions.assertEquals("result", str.get());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void printVersionFails(){
        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printConfigSchema() throws IOException {}
            @Override
            public void printHelp() {}
            @Override
            public void printVersion() throws IOException {
                throw new RuntimeException("mayday");
            }
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {}
        });
        int exitCode = cliImpl.run(new String[]{"-v"});
        Assertions.assertEquals(GapaCommandLineExceptionType.PRINT_VERSION_FAILED.getCode(), exitCode);
    }

    @Test
    void printConfigSchemaFails() {
        cliImpl.setInfoPrinter(new GapaInfoPrinter() {
            @Override
            public void printConfigSchema() throws IOException {
                throw new RuntimeException("mayday");
            }
            @Override
            public void printHelp() {}
            @Override
            public void printVersion() throws IOException {}
            @Override
            public void printCommandLineException(CommandLineException commandLineException) {}
        });
        int exitCode = cliImpl.run(new String[]{"-s"});
        Assertions.assertEquals(GapaCommandLineExceptionType.PRINT_CONFIG_SCHEMA_FAILED.getCode(), exitCode);
    }

    @Test
    void readConfigFails() {
        cliImpl.setConfigFileReader(new ConfigFileReader() {
            @Override
            public void readConfigFile(CliInput cliInput) throws Throwable {
                throw new Throwable("Little! Watch out!");
            }

            @Override
            public void readConfigFile(CliInput cliInput, Path configFilePath) throws Throwable {
                throw new Throwable("Asta la vista, baby!");
            }
        });
        int exitCode = cliImpl.run(new String[]{"-c","asdf"});
        Assertions.assertEquals(GapaCommandLineExceptionType.CONFIG_FILE_READING_FAILED.getCode(), exitCode);

        int exitCode2 = cliImpl.run(new String[]{});
        Assertions.assertEquals(GapaCommandLineExceptionType.CONFIG_FILE_READING_FAILED.getCode(), exitCode2);
    }

    @Test
    void readArgumentsFailed() {
        cliImpl.setCommandLineArgumentsReader(new CommandLineArgumentsReader() {
            @Override
            public void read(CliInput cliInput, CommandLine commandLine) throws Throwable {
                throw new Throwable("I told you!");
            }
        });
        int exitCode = cliImpl.run(new String[]{"-n","name"});
        Assertions.assertEquals(GapaCommandLineExceptionType.COMMAND_LINE_CONFIG_READING_FAILED.getCode(), exitCode);
    }

    @Test
    void processLayerFailed() {
        cliImpl.setProcessLayer(new ProcessLayer() {
            @Override
            public void stopRecording() {}

            @Override
            public void run(ProcessLayerInput input, AsyncTaskHandler<String> asyncHandler) throws Throwable {
                throw new Throwable("C'est la vie!");
            }
        });
        int exitCode = cliImpl.run(new String[]{});
        Assertions.assertEquals(GapaCommandLineExceptionType.PROCESS_LOGIC_FAILED.getCode(), exitCode);
    }

    @Test
    void configParsingFailed() {
        cliImpl.setCliInputParser(new CliInputParser() {
            @Override
            public void parse(CliInput cliInput, ProcessLayerInput processLayerInput) throws Throwable {
                throw new Throwable("Thanks mate! Jeez mate!");
            }
        });
        int exitCode = cliImpl.run(new String[]{});
        Assertions.assertEquals(GapaCommandLineExceptionType.CONFIG_PARSING_FAILED.getCode(), exitCode);
    }

    @Test
    void stopRecordingAfterUserInput() {
        AtomicReference<Boolean> stopRecordingCalled = new AtomicReference<>(false);
        AtomicReference<AsyncTaskHandler<String>> handler = new AtomicReference<>();
        cliImpl.setProcessLayer(new ProcessLayer() {
            @Override
            public void stopRecording() throws Throwable {
                stopRecordingCalled.set(true);
                //has to trigger resolution
                handler.get().onResult("result");
            }

            @Override
            public void run(ProcessLayerInput input, AsyncTaskHandler<String> asyncHandler) throws Throwable {
                handler.set(asyncHandler);
            }
        });
        cliImpl.setNonBlockingInputStream(new NonBlockingInputStream() {
            @Override
            public void start(InputStream in, NonBlockingInputStreamHandler nonBlockingInputStreamHandler) {
                try {
                    //simulate immediate user input
                    nonBlockingInputStreamHandler.onReadLine(" ");
                } catch (Throwable throwable) {
                    nonBlockingInputStreamHandler.onException(throwable);
                }
            }

            @Override
            public void close() {

            }
        });
        int exitCode = cliImpl.run(new String[]{});
        Assertions.assertTrue(stopRecordingCalled.get());
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    void inputReadingThrowsException() {
        cliImpl.setNonBlockingInputStream(new NonBlockingInputStream() {
            @Override
            public void start(InputStream in, NonBlockingInputStreamHandler nonBlockingInputStreamHandler) {
               nonBlockingInputStreamHandler.onException(new Throwable("**** happens"));
            }

            @Override
            public void close() {

            }
        });
        int exitCode = cliImpl.run(new String[]{});
        //TODO maybe the exception handling has to be more differentiated here.
        //TODO a process logic exception is shown the exception could be an IOException unrelated to process logic.
        Assertions.assertEquals(GapaCommandLineExceptionType.PROCESS_LOGIC_FAILED.getCode(), exitCode);
    }
}