package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.CliConfigOptions;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.config.parsing.CliInputParser;
import ch.bfh.ti.gapa.cli.config.parsing.CliInputParserImpl;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReaderImpl;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReaderImpl;
import ch.bfh.ti.gapa.cli.exception.CommandLineException;
import ch.bfh.ti.gapa.cli.exception.GapaCommandLineExceptionType;
import ch.bfh.ti.gapa.cli.log.SlimFormatter;
import ch.bfh.ti.gapa.cli.printer.*;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStream;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStreamHandler;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingInputStreamImpl;
import ch.bfh.ti.gapa.process.SynchronizedTask;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerImpl;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Main entry point for the application.
 * Parses command line arguments, loads configuration data and
 * passes the configuration to the process layer.
 */
public class CliImpl implements Cli{
    private static final Logger LOGGER = Logger.getLogger(CliImpl.class.getName());

    private ProcessLayer processLayer;
    private ConfigFileReader configFileReader;
    private CommandLineArgumentsReader commandLineArgumentsReader;
    private CliInputParser cliInputParser;
    private GapaInfoPrinter infoPrinter;
    private CliConfigOptions cliConfigOptions;
    private NonBlockingInputStream nonBlockingInputStream;
    private Consumer<String> printer;
    private CliPrintOptions cliPrintOptions;

    /**
     * @param processLayer read config data is passed to the process layer
     * @param configFileReader reads the config file
     * @param commandLineArgumentsReader reads configuration from command line arguments
     * @param cliInputParser parses read config data
     * @param infoPrinter prints data
     * @param cliConfigOptions contains allowed command line options for configuration
     * @param nonBlockingInputStream used to request input from the user
     * @param cliPrintOptions command line options to print information about the application
     */
    public CliImpl(ProcessLayer processLayer, ConfigFileReader configFileReader,
                   CommandLineArgumentsReader commandLineArgumentsReader,
                   CliInputParser cliInputParser, GapaInfoPrinter infoPrinter,
                   CliConfigOptions cliConfigOptions, NonBlockingInputStream nonBlockingInputStream, Consumer<String> printer, CliPrintOptions cliPrintOptions) {
        this.processLayer = processLayer;
        this.configFileReader = configFileReader;
        this.commandLineArgumentsReader = commandLineArgumentsReader;
        this.cliInputParser = cliInputParser;
        this.infoPrinter = infoPrinter;
        this.cliConfigOptions = cliConfigOptions;
        this.nonBlockingInputStream = nonBlockingInputStream;
        this.printer = printer;
        this.cliPrintOptions = cliPrintOptions;
    }

    private void throwOnUnrecognizedOptions(CommandLine commandLine) throws CommandLineException {
        if (commandLine.getArgs().length > 0) {
            Exception e = new Exception("Unrecognized arguments: " + commandLine.getArgList()
                    .stream().collect(Collectors.joining(", ")));
            throw new CommandLineException(GapaCommandLineExceptionType.UNRECOGNIZED_ARGUMENTS, e);
        }
    }

    /**
     * Runs the logic on the given arguments.
     * @param args command line arguments
     * @return exit code
     */
    public int run(String[] args) {
        try {
            try {
                DefaultParser defaultParser = new DefaultParser();
                CommandLine commandLine = defaultParser.parse(cliPrintOptions, args, true);

                // have they specified a print option?
                if (commandLine.getOptions().length > 0) {
                   //print option specified
                    throwOnUnrecognizedOptions(commandLine);

                    if (commandLine.hasOption("h")) {
                        infoPrinter.printHelp();
                        return 0;
                    } else if(commandLine.hasOption("v")) {
                        try {
                            infoPrinter.printVersion();
                            return 0;
                        } catch (Throwable t) {
                            throw new CommandLineException(GapaCommandLineExceptionType.PRINT_VERSION_FAILED, t);
                        }
                    } else if(commandLine.hasOption("s")) {
                        try {
                            infoPrinter.printConfigSchema();
                            return 0;
                        } catch (Throwable t) {
                            throw new CommandLineException(GapaCommandLineExceptionType.PRINT_CONFIG_SCHEMA_FAILED, t);
                        }
                    }
                } else {
                    // Try to parse conig options
                    commandLine = defaultParser.parse(cliConfigOptions, args);
                    throwOnUnrecognizedOptions(commandLine);

                    //The CliInput instance stores all configuration that is read from the config file
                    // and the command line arguments.
                    CliInput cliInput = new CliInput();

                    //if c option is given, read config file at given path.
                    //Otherwise, read config.json in same directory as jar
                    try {
                        if (commandLine.hasOption("c")) {
                            String configFilePathArg = commandLine.getOptionValue("c");
                            Path configFilePath = Paths.get(configFilePathArg);
                            configFileReader.readConfigFile(cliInput, configFilePath);
                        } else {
                            configFileReader.readConfigFile(cliInput);
                        }
                    } catch(Throwable t) {
                        throw new CommandLineException(GapaCommandLineExceptionType.CONFIG_FILE_READING_FAILED, t);
                    }

                    try {
                        commandLineArgumentsReader.read(cliInput, commandLine);
                    } catch (Throwable t) {
                        throw new CommandLineException(GapaCommandLineExceptionType.COMMAND_LINE_CONFIG_READING_FAILED, t);
                    }

                    ProcessLayerInput processLayerInput = new ProcessLayerInput();
                    try {
                        cliInputParser.parse(cliInput, processLayerInput);
                    } catch (Throwable t) {
                        throw new CommandLineException(GapaCommandLineExceptionType.CONFIG_PARSING_FAILED, t);
                    }

                    try {
                        String plantUml = syncProcessLayerStartRecording(processLayerInput);
                        printer.accept(plantUml);
                    } catch (Throwable t) {
                        throw new CommandLineException(GapaCommandLineExceptionType.PROCESS_LOGIC_FAILED, t);
                    }
                }
            } catch (ParseException e) {
                throw new CommandLineException(GapaCommandLineExceptionType.INVALID_COMMAND_USAGE, e);
            }
        } catch (CommandLineException e) {
            //Print exception message and return exit code.
            infoPrinter.printCommandLineException(e);
            return e.getCommandLineExceptionType().getCode();
        }
        return 0;
    }

    private String syncProcessLayerStartRecording(ProcessLayerInput processLayerInput) throws Throwable {
        SynchronizedTask<String, ProcessLayerInput> synchronizedTask = new SynchronizedTask<>(processLayer);

        //Listen to stdin when websocket connection was started
        //This allows to stop recording when user pressed enter key
        Runnable runAfter = () -> nonBlockingInputStream.start(System.in, new NonBlockingInputStreamHandler() {
            @Override
            public void onReadLine(String line) throws Throwable {
                processLayer.stopRecording();
            }

            @Override
            public void onException(Throwable t) {
                //within the non blocking input stream or in the code called by onReadLine
                //was an exception thrown. We stop the synchronized task immediately with the exception.
                synchronizedTask.outsideExceptionInterrupts(t);
            }
        });

        return synchronizedTask.run(processLayerInput, runAfter);
    }

    public void setProcessLayer(ProcessLayer processLayer) {
        this.processLayer = processLayer;
    }

    public void setConfigFileReader(ConfigFileReader configFileReader) {
        this.configFileReader = configFileReader;
    }

    public void setCommandLineArgumentsReader(CommandLineArgumentsReader commandLineArgumentsReader) {
        this.commandLineArgumentsReader = commandLineArgumentsReader;
    }

    public void setCliInputParser(CliInputParser cliInputParser) {
        this.cliInputParser = cliInputParser;
    }

    public void setInfoPrinter(GapaInfoPrinter infoPrinter) {
        this.infoPrinter = infoPrinter;
    }

    public void setNonBlockingInputStream(NonBlockingInputStream nonBlockingInputStream) {
        this.nonBlockingInputStream = nonBlockingInputStream;
    }

    public void setPrinter(Consumer<String> printer) {
        this.printer = printer;
    }
}
