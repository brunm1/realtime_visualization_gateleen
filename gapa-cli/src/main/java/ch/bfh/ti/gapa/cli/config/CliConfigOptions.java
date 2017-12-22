package ch.bfh.ti.gapa.cli.config;

import ch.bfh.ti.gapa.cli.config.model.ConfigField;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * Defines command line arguments for configuration.
 */
public class CliConfigOptions extends Options {
    public CliConfigOptions() {
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
        this.addOption(serverName);
    }
}
