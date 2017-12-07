package ch.bfh.ti.gapa.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

class CliOptions extends Options {
    CliOptions() {
            OptionGroup printOptionGroup = new OptionGroup();

            Option help = Option.builder("h")
                    .desc("Shows this help")
                    .longOpt("help")
                    .build();

            Option version = Option.builder("v")
                    .desc("Print version number.")
                    .longOpt("version")
                    .build();

            Option jsonConfigSchema = Option.builder("s")
                    .desc("Print json config schema.")
                    .longOpt("schema")
                    .build();

            printOptionGroup.addOption(help);
            printOptionGroup.addOption(version);
            printOptionGroup.addOption(jsonConfigSchema);

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

            this.addOption(websocketUri);
            this.addOption(config);
            this.addOptionGroup(printOptionGroup);
    }
}
