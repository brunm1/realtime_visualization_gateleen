package ch.bfh.ti.gapa.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

class CliOptions {
    static Options options = new Options();

    static {
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

        Option config = Option.builder("c")
                .desc("Set the path to a user config file.")
                .hasArg()
                .argName("path")
                .longOpt("config")
                .build();

        Option version = Option.builder("v")
                .desc("Print version number.")
                .longOpt("version")
                .build();

        options.addOption(help);
        options.addOption(websocketUri);
        options.addOption(config);
        options.addOption(version);
    }
}
