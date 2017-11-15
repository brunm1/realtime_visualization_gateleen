package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;
import ch.bfh.ti.gapa.process.recording.InboundRequestPatternGroup;
import ch.bfh.ti.gapa.process.recording.OutboundRequestPatternGroup;
import ch.bfh.ti.gapa.process.recording.RecordParser;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class Cli {

    private static Options getOptions() {
        Options options = new Options();

        String inboundRequestPatternGroups = Arrays.stream(InboundRequestPatternGroup.values())
                .map(InboundRequestPatternGroup::getName)
                .collect(Collectors.joining(", "));

        Option inboundRequestPattern = Option.builder("i")
                .hasArg()
                .argName("pattern")
                .desc("The regex pattern for inbound requests. Must contain regex groups: " + inboundRequestPatternGroups)
                .longOpt("inbound-request-pattern")
                .build();

        String outboundRequestPatternGroups = Arrays.stream(OutboundRequestPatternGroup.values())
                .map(OutboundRequestPatternGroup::getName)
                .collect(Collectors.joining(", "));

        Option outboundRequestPattern = Option.builder("o")
                .hasArg()
                .argName("pattern")
                .desc("The regex pattern for outbound requests. Must contain regex groups: " + outboundRequestPatternGroups)
                .longOpt("outbound-request-pattern")
                .build();

        Option timeFormat = Option.builder("t")
                .hasArg()
                .argName("pattern")
                .desc("The time pattern. The format is specified by DateTimeFormatter from Java 8 Standard Library. Locale.GERMANY will be used.")
                .longOpt("time-format")
                .build();

        Option fileName = Option.builder("f")
                .hasArg()
                .argName("file")
                .desc("Path to a file containing logs.")
                .longOpt("file-name")
                .build();

        options.addOption(inboundRequestPattern);
        options.addOption(outboundRequestPattern);
        options.addOption(timeFormat);
        options.addOption(fileName);

        return options;
    }

    private static Options options = getOptions();

    static class CommandLineException extends Throwable {
        private Error error;
        private Throwable throwable;

        public CommandLineException(Error error, Throwable throwable) {
            this.error = error;
            this.throwable = throwable;
        }

        public Error getError() {
            return error;
        }

        public void setError(Error error) {
            this.error = error;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
        }
    }

    static Pattern defaultInboundRequestPattern =
            Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})" +
                    "\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+) - %(\\S+)\\s+" +
                    "(?<method>GET|PUT|POST|DELETE)" +
                    "\\s+" +
                    "(?<url>\\S+)" +
                    "\\s+s=" +
                    "(?<sender>\\w+)");

    static Pattern defaultOutboundRequestPattern =
            Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) " +
                    "(?<method>GET|PUT|POST|DELETE) " +
                    "(?<url>\\S+) " +
                    "(?<receiver>\\w+)");

    static Locale usedLocale = Locale.GERMANY;

    static DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS", usedLocale);

    public static void printHelp() {
        System.out.println("Visualization of communication between services powered by Gateleen. It parses logs and outputs plantuml. " +
                "If no -f option is given, stdin is used. Logs must be in utf8.");
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("", options);
    }

    enum Error {
        FILE_NOT_FOUND(1, "Could not find file."),
        FAILED_PARSING_INBOUND_REQUEST_PATTERN(2, "Could not parse inbound request pattern."),
        FAILED_PARSING_OUTBOUND_REQUEST_PATTERN(3, "Could not parse outbound request pattern."),
        FAILED_PARSING_DATE_TIME_PATTERN(4, "Could not parse date time pattern."),
        INVALID_COMMAND_USAGE(5, "Invalid command usage.");

        private int code;
        private String desc;

        Error(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static void exitWithError(CommandLineException e) {
        System.out.println(e.getError().getDesc() + " Cause: " + e.getThrowable().getMessage());
        System.exit(e.getError().getCode());
    }

    public static void main(String[] args) throws IOException {
        DefaultParser defaultParser = new DefaultParser();
        try {
            try {
                CommandLine commandLine = defaultParser.parse(options, args);

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


                //TODO should be more encapsulated (cli should pass input to somewhere else)

                //parse logs and produce records
                RecordParser recordParser =
                        new RecordParser(input.getInboundRequestPattern(),
                                /*TODO change/remove */ Pattern.compile("(?<target>.*)"),
                                input.getDateTimeFormatter());
                String log = StringFromInputStreamReader.readStringFromInputStream(input.getInputStream(), Charset.forName("UTF-8"), 1024);
                List<Record> records = recordParser.batchParse(log);

                //process records and output plantUml diagram
                String plantUmlDiagram = new SequenceDiagramGenerator().generatePlantUmlSequenceDiagramFromRecords(records);
                System.out.println(plantUmlDiagram);
            } catch (ParseException e) {
                throw new CommandLineException(Error.INVALID_COMMAND_USAGE, e);
            }
        } catch (CommandLineException e) {
            //Print error message and exit with error code.
            exitWithError(e);
        }
    }
}
