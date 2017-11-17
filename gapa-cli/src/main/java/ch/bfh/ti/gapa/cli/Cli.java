package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.process.interfaces.Input;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayer;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static ch.bfh.ti.gapa.cli.CliOptions.options;
import static ch.bfh.ti.gapa.cli.Defaults.*;
import static ch.bfh.ti.gapa.cli.HelpPrinter.printHelp;

public class Cli {
    private ProcessLayer processLayer;

    Cli(ProcessLayer processLayer) {
        this.processLayer = processLayer;
    }

    private void printError(CommandLineException e) {
        System.out.println(e.getError().getDesc() + " Cause: " + e.getThrowable().getMessage());
    }

    int run(String[] args) throws IOException {
        DefaultParser defaultParser = new DefaultParser();
        try {
            try {
                CommandLine commandLine = defaultParser.parse(options, args);

                if(commandLine.hasOption("h")) {
                    printHelp();
                    return 0;
                }

                if(commandLine.getArgs().length>0) {
                    Exception e = new Exception(commandLine.getArgList().stream().collect(Collectors.joining(", ")));
                    throw new CommandLineException(Error.UNRECOGNIZED_ARGUMENTS, e);
                }

                //the input object contains the parsed arguments
                Input input = new Input();

                //try to parse every command line argument and fill the input object with given arguments or
                //default values
                if (commandLine.hasOption("i")) {
                    Pattern inboundRequestPattern;
                    try {
                        inboundRequestPattern = Pattern.compile(commandLine.getOptionValue("i"));
                    } catch (PatternSyntaxException e) {
                        throw new CommandLineException(Error.FAILED_PARSING_INBOUND_REQUEST_PATTERN, e);
                    }
                    input.setInboundRequestPattern(inboundRequestPattern);
                } else {
                    input.setInboundRequestPattern(defaultInboundRequestPattern);
                }

                if (commandLine.hasOption("o")) {
                    Pattern inboundRequestPattern;
                    try {
                        inboundRequestPattern = Pattern.compile(commandLine.getOptionValue("i"));
                    } catch (PatternSyntaxException e) {
                        throw new CommandLineException(Error.FAILED_PARSING_OUTBOUND_REQUEST_PATTERN, e);
                    }
                    input.setInboundRequestPattern(inboundRequestPattern);
                } else {
                    input.setOutboundRequestPattern(defaultOutboundRequestPattern);
                }

                if (commandLine.hasOption("t")) {
                    DateTimeFormatter dateTimeFormatter;
                    try {
                        dateTimeFormatter = DateTimeFormatter.ofPattern(commandLine.getOptionValue("t"), usedLocale);
                    } catch (IllegalArgumentException e) {
                        throw new CommandLineException(Error.FAILED_PARSING_DATE_TIME_PATTERN, e);
                    }
                    input.setDateTimeFormatter(dateTimeFormatter);
                } else {
                    input.setDateTimeFormatter(defaultDateTimeFormatter);
                }

                if (commandLine.hasOption("f")) {
                    String fileName = commandLine.getOptionValue("f");
                    try {
                        System.setIn(new FileInputStream(fileName));
                    } catch (FileNotFoundException e) {
                        throw new CommandLineException(Error.FILE_NOT_FOUND, e);
                    }
                }
                input.setInputStream(System.in);

                String plantUmlDiagram = processLayer.process(input);

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

    public static void main(String[] args) throws IOException {
        int exitCode = new Cli(new ProcessLayerImpl()).run(args);
        System.exit(exitCode);
    }
}
