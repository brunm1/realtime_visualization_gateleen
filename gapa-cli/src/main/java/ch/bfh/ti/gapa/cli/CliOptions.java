package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.cli.config.ConfigField;
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
                    .desc("Connect over websocket to this URI. Default: " + ConfigField.websocketUri.getDefaultValue())
                    .hasArg()
                    .argName("uri")
                    .longOpt("websocket")
                    .build();

            Option serverName = Option.builder("n")
                    .desc("Name of the central server that receives and sends requests. Default: " + ConfigField.serverName.getDefaultValue())
                    .hasArg()
                    .argName("name")
                    .longOpt("server-name")
                    .build();

            Option config = Option.builder("c")
                    .desc("Set the path to config file. Default: path to config.json in the same directory as executed jar")
                    .hasArg()
                    .argName("path")
                    .longOpt("config")
                    .build();

            this.addOption(websocketUri);
            this.addOption(config);
            this.addOptionGroup(printOptionGroup);
            this.addOption(serverName);
    }
}
