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
import ch.bfh.ti.gapa.process.interfaces.Input;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.stream.Collectors;

import static ch.bfh.ti.gapa.cli.CliOptions.options;
import static ch.bfh.ti.gapa.cli.HelpPrinter.printHelp;

public class Cli {
    private ProcessLayer processLayer;
    private DefaultConfigFileReader defaultConfigFileReader;
    private CommandLineArgumentsReader commandLineArgumentsReader;
    private RawInputParser rawInputParser;

    public Cli(ProcessLayer processLayer, DefaultConfigFileReader defaultConfigFileReader, CommandLineArgumentsReader commandLineArgumentsReader, RawInputParser rawInputParser) {
        this.processLayer = processLayer;
        this.defaultConfigFileReader = defaultConfigFileReader;
        this.commandLineArgumentsReader = commandLineArgumentsReader;
        this.rawInputParser = rawInputParser;
    }

    private void printError(CommandLineException e) {
        System.out.println(e.getExceptionType().getDesc() + " Cause: " + e.getThrowable().getMessage());
    }

    int run(String[] args) {
        try {
            try {
                DefaultParser defaultParser = new DefaultParser();
                CommandLine commandLine = defaultParser.parse(options, args);

                if (commandLine.hasOption("h")) {
                    printHelp();
                    return 0;
                }

                if (commandLine.getArgs().length > 0) {
                    Exception e = new Exception(commandLine.getArgList().stream().collect(Collectors.joining(", ")));
                    throw new CommandLineException(ExceptionType.UNRECOGNIZED_ARGUMENTS, e);
                }

                //The RawInput instance stores all configuration that is read in from the default
                //config file, the user given config file and the command line arguments.
                RawInput rawInput = new RawInput();

                try {
                    defaultConfigFileReader.read(rawInput);
                } catch (Throwable t) {
                    throw new CommandLineException(ExceptionType.DEFAULT_CONFIG_LOADING_FAILED, t);
                }

                try {
                    commandLineArgumentsReader.read(rawInput, commandLine);
                } catch (Throwable t) {
                    throw new CommandLineException(ExceptionType.USER_CONFIG_LOADING_FAILED, t);
                }

                Input input = new Input();
                try {
                    rawInputParser.parse(rawInput, input);
                } catch (Throwable t) {
                    throw new CommandLineException(ExceptionType.CONFIG_PARSING_FAILED, t);
                }

                String plantUmlDiagram;
                try {
                    plantUmlDiagram = processLayer.process(input);
                } catch (Throwable t) {
                    throw new CommandLineException(ExceptionType.PROCESS_LOGIC_FAILED, t);
                }

                System.out.print(plantUmlDiagram);
            } catch (ParseException e) {
                throw new CommandLineException(ExceptionType.INVALID_COMMAND_USAGE, e);
            }
        } catch (CommandLineException e) {
            //Print error message and exit with error code.
            printError(e);
            return e.getExceptionType().getCode();
        }
        return 0;
    }

    public static void main(String[] args) {
        ProcessLayer processLayer = new ProcessLayerImpl();
        JsonConfigValidator jsonConfigValidator = new JsonConfigValidatorImpl();
        JsonReader jsonReader = new JsonReaderImpl();
        RawInputParser rawInputParser = new RawInputParserImpl();
        DefaultConfigFileReader defaultConfigFileReader = new DefaultConfigFileReaderImpl(
                jsonConfigValidator, jsonReader
        );
        CommandLineArgumentsReaderImpl commandLineArgumentsLoader = new CommandLineArgumentsReaderImpl(
                jsonConfigValidator,
                jsonReader,
                rawInputParser
        );

        int exitCode = new Cli(processLayer, defaultConfigFileReader, commandLineArgumentsLoader, rawInputParser).run(args);
        System.exit(exitCode);
    }
}
