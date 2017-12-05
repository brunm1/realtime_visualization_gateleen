package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.json.converter.JsonToRawInputConverter;
import ch.bfh.ti.gapa.cli.json.converter.JsonToRawInputConverterImpl;
import ch.bfh.ti.gapa.cli.json.validation.ConfigFileValidator;
import ch.bfh.ti.gapa.cli.json.validation.ConfigFileValidatorImpl;
import ch.bfh.ti.gapa.cli.loader.CommandLineArgumentsLoader;
import ch.bfh.ti.gapa.cli.loader.CommandLineArgumentsLoaderImpl;
import ch.bfh.ti.gapa.cli.loader.DefaultConfigLoader;
import ch.bfh.ti.gapa.cli.loader.DefaultConfigLoaderImpl;
import ch.bfh.ti.gapa.cli.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.parsing.RawInputParserImpl;
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

    private DefaultConfigLoader defaultConfigLoader;
    private CommandLineArgumentsLoader commandLineArgumentsLoader;

    public Cli(ProcessLayer processLayer, DefaultConfigLoader defaultConfigLoader, CommandLineArgumentsLoader commandLineArgumentsLoader) {
        this.processLayer = processLayer;
        this.defaultConfigLoader = defaultConfigLoader;
        this.commandLineArgumentsLoader = commandLineArgumentsLoader;
    }

    private void printError(CommandLineException e) {
        System.out.println(e.getError().getDesc() + " Cause: " + e.getThrowable().getMessage());
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
                    throw new CommandLineException(Error.UNRECOGNIZED_ARGUMENTS, e);
                }

                //The input type is defined in the process layer and contains all data that will be passed to the process layer.
                Input input = new Input();

                try {
                    defaultConfigLoader.loadInput(input);
                } catch (Throwable t) {
                    throw new CommandLineException(Error.DEFAULT_CONFIG_LOADING_FAILED, t);
                }

                try {
                    commandLineArgumentsLoader.loadInput(input, commandLine);
                } catch (Throwable t) {
                    throw new CommandLineException(Error.USER_CONFIG_LOADING_FAILED, t);
                }

                String plantUmlDiagram;
                try {
                    plantUmlDiagram = processLayer.process(input);
                } catch (Throwable t) {
                    throw new CommandLineException(Error.PROCESS_LOGIC_FAILED, t);
                }

                System.out.print(plantUmlDiagram);
            } catch (ParseException e) {
                throw new CommandLineException(Error.INVALID_COMMAND_USAGE, e);
            }
        } catch (CommandLineException e) {
            //Print error message and exit with error code.
            printError(e);
            return e.getError().getCode();
        }
        return 0;
    }

    public static void main(String[] args) {
        ProcessLayer processLayer = new ProcessLayerImpl();
        ConfigFileValidator configFileValidator = new ConfigFileValidatorImpl();
        JsonToRawInputConverter jsonToRawInputConverter = new JsonToRawInputConverterImpl();
        RawInputParser rawInputParser = new RawInputParserImpl();
        DefaultConfigLoader defaultConfigLoader = new DefaultConfigLoaderImpl(
                configFileValidator, jsonToRawInputConverter, rawInputParser
        );
        CommandLineArgumentsLoaderImpl commandLineArgumentsLoader = new CommandLineArgumentsLoaderImpl(
                configFileValidator,
                jsonToRawInputConverter,
                rawInputParser
        );

        int exitCode = new Cli(processLayer, defaultConfigLoader, commandLineArgumentsLoader).run(args);
        System.exit(exitCode);
    }
}
