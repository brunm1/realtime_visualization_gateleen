package ch.bfh.ti.gapa.cli.config.reading.commandline;

import ch.bfh.ti.gapa.cli.config.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;
import org.apache.commons.cli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reads configuration data from the command line arguments.
 */
public class CommandLineArgumentsReaderImpl implements CommandLineArgumentsReader {

    private JsonConfigValidator jsonConfigValidator;
    private JsonReader jsonReader;

    public CommandLineArgumentsReaderImpl(JsonConfigValidator jsonConfigValidator, JsonReader jsonReader, RawInputParser rawInputParser) {
        this.jsonConfigValidator = jsonConfigValidator;
        this.jsonReader = jsonReader;
    }

    /**
     * If the c command line option is given, it's config file will be loaded first.
     * After that, all other options overwrite the configuration read from the
     * default config file an the config file given by the c option.
     * @param rawInput A RawInput instance. Configuration is read into this instance.
     * @param commandLine An instance of type CommandLine which contains command line options.
     */
    public void read(RawInput rawInput, CommandLine commandLine) {
        if(commandLine.hasOption("c")) {
            Path userConfigPath = Paths.get(commandLine.getOptionValue("c"));

            ConfigFileReader configFileReader = new ConfigFileReader(jsonConfigValidator,jsonReader) {
                @Override
                protected Path createConfigFilePath() {
                    return userConfigPath;
                }
            };

            configFileReader.readMandatoryConfigFile(rawInput);
        }

        if(commandLine.hasOption("w")) {
            rawInput.setWebsocketUri(commandLine.getOptionValue("w"));
        }
    }
}
