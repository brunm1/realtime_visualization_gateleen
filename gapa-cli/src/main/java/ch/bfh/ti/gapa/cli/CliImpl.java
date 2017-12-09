package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReaderImpl;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidatorImpl;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReader;
import ch.bfh.ti.gapa.cli.config.reading.commandline.CommandLineArgumentsReaderImpl;
import ch.bfh.ti.gapa.cli.config.reading.file.DefaultConfigFileReader;
import ch.bfh.ti.gapa.cli.config.reading.file.DefaultConfigFileReaderImpl;
import ch.bfh.ti.gapa.cli.config.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.config.parsing.RawInputParserImpl;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.cli.exception.CommandLineException;
import ch.bfh.ti.gapa.cli.exception.CommandLineExceptionType;
import ch.bfh.ti.gapa.cli.printer.InfoPrinter;
import ch.bfh.ti.gapa.process.interfaces.Input;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerImpl;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Main entry point for the application.
 * Parses command line arguments, loads configuration data and
 * passes the configuration to the process layer.
 */
public class CliImpl implements Cli{
    private ProcessLayer processLayer;
    private DefaultConfigFileReader defaultConfigFileReader;
    private CommandLineArgumentsReader commandLineArgumentsReader;
    private RawInputParser rawInputParser;
    private InfoPrinter infoPrinter;
    private CliOptions cliOptions;
    private InputSupplier inputSupplier;

    /**
     * @param processLayer read config data is passed to the process layer
     * @param defaultConfigFileReader reads the default config file
     * @param commandLineArgumentsReader reads configuration from command line arguments
     * @param rawInputParser parses read config data
     * @param infoPrinter prints data to stdout
     * @param cliOptions contains allowed command line options
     * @param inputSupplier used to request input from the user
     */
    public CliImpl(ProcessLayer processLayer, DefaultConfigFileReader defaultConfigFileReader,
                   CommandLineArgumentsReader commandLineArgumentsReader,
                   RawInputParser rawInputParser, InfoPrinter infoPrinter,
                   CliOptions cliOptions, InputSupplier inputSupplier) {
        this.processLayer = processLayer;
        this.defaultConfigFileReader = defaultConfigFileReader;
        this.commandLineArgumentsReader = commandLineArgumentsReader;
        this.rawInputParser = rawInputParser;
        this.infoPrinter = infoPrinter;
        this.cliOptions = cliOptions;
        this.inputSupplier = inputSupplier;
    }

    /**
     * Create CliImpl with default dependencies
     */
    public CliImpl() {
        ProcessLayer processLayer = new ProcessLayerImpl();
        JsonConfigValidator jsonConfigValidator = new JsonConfigValidatorImpl();
        JsonReader jsonReader = new JsonReaderImpl();
        RawInputParser rawInputParser = new RawInputParserImpl();

        DefaultConfigFileReader defaultConfigFileReader = new DefaultConfigFileReaderImpl(
                jsonConfigValidator, jsonReader
        );

        CommandLineArgumentsReaderImpl commandLineArgumentsReader = new CommandLineArgumentsReaderImpl(
                jsonConfigValidator,
                jsonReader
        );

        CliOptions cliOptions = new CliOptions();

        InfoPrinter infoPrinter = new InfoPrinter(cliOptions);

        this.processLayer = processLayer;
        this.defaultConfigFileReader = defaultConfigFileReader;
        this.commandLineArgumentsReader = commandLineArgumentsReader;
        this.rawInputParser = rawInputParser;
        this.infoPrinter = infoPrinter;
        this.cliOptions = cliOptions;
        this.inputSupplier = () -> {
            try {
                System.in.read();
            } catch (IOException e) {
                //TODO handle input exception meaningful
                e.printStackTrace();
            }
            return "";
        };
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

                    //The RawInput instance stores all configuration that is read in from the default
                    //config file, the user given config file and the command line arguments.
                    RawInput rawInput = new RawInput();

                    try {
                        defaultConfigFileReader.read(rawInput);
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.DEFAULT_CONFIG_READING_FAILED, t);
                    }

                    try {
                        commandLineArgumentsReader.read(rawInput, commandLine);
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.COMMAND_LINE_CONFIG_READING_FAILED, t);
                    }

                    Input input = new Input();
                    try {
                        rawInputParser.parse(rawInput, input);
                    } catch (Throwable t) {
                        throw new CommandLineException(CommandLineExceptionType.CONFIG_PARSING_FAILED, t);
                    }

                    //TODO additional validation step to avoid nullpointers (websocket has to be present)

                    try {
                        processLayer.startRecording(input);

                        //wait until user gives input
                        inputSupplier.requestInput();

                        //stop recording
                        String plantUmlDiagram = processLayer.stopRecording();
                        System.out.println(plantUmlDiagram);
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

    /**
     * Runs the CLI with the given arguments and exits the application with an exit code.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        int exitCode = new CliImpl().run(args);

        System.exit(exitCode);
    }

    public ProcessLayer getProcessLayer() {
        return processLayer;
    }

    public void setProcessLayer(ProcessLayer processLayer) {
        this.processLayer = processLayer;
    }

    public DefaultConfigFileReader getDefaultConfigFileReader() {
        return defaultConfigFileReader;
    }

    public void setDefaultConfigFileReader(DefaultConfigFileReader defaultConfigFileReader) {
        this.defaultConfigFileReader = defaultConfigFileReader;
    }

    public CommandLineArgumentsReader getCommandLineArgumentsReader() {
        return commandLineArgumentsReader;
    }

    public void setCommandLineArgumentsReader(CommandLineArgumentsReader commandLineArgumentsReader) {
        this.commandLineArgumentsReader = commandLineArgumentsReader;
    }

    public RawInputParser getRawInputParser() {
        return rawInputParser;
    }

    public void setRawInputParser(RawInputParser rawInputParser) {
        this.rawInputParser = rawInputParser;
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

    public InputSupplier getInputSupplier() {
        return inputSupplier;
    }

    public void setInputSupplier(InputSupplier inputSupplier) {
        this.inputSupplier = inputSupplier;
    }
}
