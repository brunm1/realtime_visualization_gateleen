package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.process.recording.InboundRequestPatternGroup;
import ch.bfh.ti.gapa.process.recording.OutboundRequestPatternGroup;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ch.bfh.ti.gapa.cli.Defaults.defaultDateTimeFormatterStr;
import static ch.bfh.ti.gapa.cli.Defaults.defaultInboundRequestPatternStr;
import static ch.bfh.ti.gapa.cli.Defaults.defaultOutboundRequestPatternStr;

class CliOptions {
    static Options options = new Options();

    static {

        String inboundRequestPatternGroups = Arrays.stream(InboundRequestPatternGroup.values())
                .map(InboundRequestPatternGroup::getName)
                .collect(Collectors.joining(", "));

        Option inboundRequestPattern = Option.builder("i")
                .hasArg()
                .argName("pattern")
                .desc("The regex pattern for inbound requests. Must contain regex groups: " + inboundRequestPatternGroups + ".\n"+
                        "Default: \"" + defaultInboundRequestPatternStr + "\"")
                .longOpt("inbound-request-pattern")
                .build();

        String outboundRequestPatternGroups = Arrays.stream(OutboundRequestPatternGroup.values())
                .map(OutboundRequestPatternGroup::getName)
                .collect(Collectors.joining(", "));

        Option outboundRequestPattern = Option.builder("o")
                .hasArg()
                .argName("pattern")
                .desc("The regex pattern for outbound requests. Must contain regex groups: " + outboundRequestPatternGroups + ".\n"+
                        "Default: \"" + defaultOutboundRequestPatternStr + "\"")
                .longOpt("outbound-request-pattern")
                .build();

        Option timeFormat = Option.builder("t")
                .hasArg()
                .argName("pattern")
                .desc("The time pattern used for the regex group 'date'. The format is specified by DateTimeFormatter from Java 8 Standard Library." +
                        "Locale.GERMANY will be used.\n" +
                        "Default: \"" + defaultDateTimeFormatterStr + "\"")
                .longOpt("time-format")
                .build();

        Option fileName = Option.builder("f")
                .hasArg()
                .argName("file")
                .desc("Path to a file containing logs.")
                .longOpt("file-name")
                .build();

        Option help = Option.builder("h")
                .desc("Shows this help")
                .longOpt("help")
                .build();

        Option websocketUri = Option.builder("w")
                .desc("Connect over websocket to this URI.")
                .hasArg()
                .argName("uri")
                .longOpt("websocket")
                .build();

        options.addOption(inboundRequestPattern);
        options.addOption(outboundRequestPattern);
        options.addOption(timeFormat);
        options.addOption(fileName);
        options.addOption(help);
        options.addOption(websocketUri);
    }
}
