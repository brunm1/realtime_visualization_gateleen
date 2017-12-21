package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.config.parsing.CliInputParser;
import ch.bfh.ti.gapa.cli.config.parsing.CliInputParserImpl;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReaderImpl;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReaderImpl;
import ch.bfh.ti.gapa.cli.exception.CommandLineException;
import ch.bfh.ti.gapa.cli.exception.CommandLineExceptionType;
import ch.bfh.ti.gapa.cli.log.SlimFormatter;
import ch.bfh.ti.gapa.cli.printer.InfoPrinter;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingStdIn;
import ch.bfh.ti.gapa.cli.stdin.NonBlockingStdInImpl;
import ch.bfh.ti.gapa.process.SynchronizedTask;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerImpl;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

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
    private InfoPrinter infoPrinter;
    private CliOptions cliOptions;
    private NonBlockingStdIn nonBlockingStdIn;
    private Consumer<String> printer;

    /**
     * @param processLayer read config data is passed to the process layer
     * @param configFileReader reads the config file
     * @param commandLineArgumentsReader reads configuration from command line arguments
     * @param cliInputParser parses read config data
     * @param infoPrinter prints data
     * @param cliOptions contains allowed command line options
     * @param nonBlockingStdIn used to request input from the user
     */
    public CliImpl(ProcessLayer processLayer, ConfigFileReader configFileReader,
                   CommandLineArgumentsReader commandLineArgumentsReader,
                   CliInputParser cliInputParser, InfoPrinter infoPrinter,
                   CliOptions cliOptions, NonBlockingStdIn nonBlockingStdIn, Consumer<String> printer) {
        this.processLayer = processLayer;
        this.configFileReader = configFileReader;
        this.commandLineArgumentsReader = commandLineArgumentsReader;
        this.cliInputParser = cliInputParser;
        this.infoPrinter = infoPrinter;
        this.cliOptions = cliOptions;
        this.nonBlockingStdIn = nonBlockingStdIn;
        this.printer = printer;
    }

    /**
     * Create CliImpl with default dependencies
     */
    public CliImpl() {
        ProcessLayer processLayer = new ProcessLayerImpl();
        CliInputParser cliInputParser = new CliInputParserImpl();

        ConfigFileReader configFileReader = new ConfigFileReaderImpl();

        CommandLineArgumentsReaderImpl commandLineArgumentsReader = new CommandLineArgumentsReaderImpl();

        CliOptions options = new CliOptions();
        InfoPrinter infoPrinter = new InfoPrinter(options);

        this.processLayer = processLayer;
        this.configFileReader = configFileReader;
        this.commandLineArgumentsReader = commandLineArgumentsReader;
        this.cliInputParser = cliInputParser;
        this.infoPrinter = infoPrinter;
        this.cliOptions = options;
        this.nonBlockingStdIn = new NonBlockingStdInImpl();
        this.printer = System.out::println;
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
                CommandLine commandLine = defaultParser.parse(cliOptions, args);

                if (commandLine.hasOption("h")) {
                    infoPrinter.printHelp();
                    return 0;
                } else if(commandLine.hasOption("v")) {
                    try {
                        infoPrinter.printVersion();
                        return 0;
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.PRINT_VERSION_FAILED, t);
                    }
                } else if(commandLine.hasOption("s")) {
                    try {
                        infoPrinter.printConfigSchema();
                        return 0;
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.PRINT_CONFIG_SCHEMA_FAILED, t);
                    }
                } else {
                    if (commandLine.getArgs().length > 0) {
                        Exception e = new Exception("Unrecognized arguments: " + commandLine.getArgList()
                                .stream().collect(Collectors.joining(", ")));
                        throw new CommandLineException(CommandLineExceptionType.UNRECOGNIZED_ARGUMENTS, e);
                    }

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
                        throw new CommandLineException(CommandLineExceptionType.CONFIG_FILE_READING_FAILED, t);
                    }

                    try {
                        commandLineArgumentsReader.read(cliInput, commandLine);
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.COMMAND_LINE_CONFIG_READING_FAILED, t);
                    }

                    ProcessLayerInput processLayerInput = new ProcessLayerInput();
                    try {
                        cliInputParser.parse(cliInput, processLayerInput);
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.CONFIG_PARSING_FAILED, t);
                    }

                    try {
                        String plantUml = syncProcessLayerStartRecording(processLayerInput);
                        printer.accept(plantUml);
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.PROCESS_LOGIC_FAILED, t);
                    }
                }
            } catch (ParseException e) {
                throw new CommandLineException(CommandLineExceptionType.INVALID_COMMAND_USAGE, e);
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
        Runnable runAfter = () -> nonBlockingStdIn.start(line -> processLayer.stopRecording());

        return synchronizedTask.run(processLayerInput, runAfter);
    }

    /**
     * Runs the CLI with the given arguments and exits the application with an exit code.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        //configure custom log format
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new SlimFormatter());
        int exitCode = new CliImpl().run(args);

        System.exit(exitCode);
    }

    public ProcessLayer getProcessLayer() {
        return processLayer;
    }

    public void setProcessLayer(ProcessLayer processLayer) {
        this.processLayer = processLayer;
    }

    public ConfigFileReader getConfigFileReader() {
        return configFileReader;
    }

    public void setConfigFileReader(ConfigFileReader configFileReader) {
        this.configFileReader = configFileReader;
    }

    public CommandLineArgumentsReader getCommandLineArgumentsReader() {
        return commandLineArgumentsReader;
    }

    public void setCommandLineArgumentsReader(CommandLineArgumentsReader commandLineArgumentsReader) {
        this.commandLineArgumentsReader = commandLineArgumentsReader;
    }

    public CliInputParser getCliInputParser() {
        return cliInputParser;
    }

    public void setCliInputParser(CliInputParser cliInputParser) {
        this.cliInputParser = cliInputParser;
    }

    public InfoPrinter getInfoPrinter() {
        return infoPrinter;
    }

    public void setInfoPrinter(InfoPrinter infoPrinter) {
        this.infoPrinter = infoPrinter;
    }

    public CliOptions getCliOptions() {
        return cliOptions;
    }

    public void setCliOptions(CliOptions cliOptions) {
        this.cliOptions = cliOptions;
    }

    public NonBlockingStdIn getNonBlockingStdIn() {
        return nonBlockingStdIn;
    }

    public void setNonBlockingStdIn(NonBlockingStdIn nonBlockingStdIn) {
        this.nonBlockingStdIn = nonBlockingStdIn;
    }

    public Consumer<String> getPrinter() {
        return printer;
    }

    public void setPrinter(Consumer<String> printer) {
        this.printer = printer;
    }
}
