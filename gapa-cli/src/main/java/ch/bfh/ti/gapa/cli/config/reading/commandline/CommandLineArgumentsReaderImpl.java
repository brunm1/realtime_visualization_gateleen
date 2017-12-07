package ch.bfh.ti.gapa.cli.config.reading.commandline;

import ch.bfh.ti.gapa.cli.config.reading.file.ConfigFileReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import org.apache.commons.cli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reads configuration data from the command line arguments.
 */
public class CommandLineArgumentsReaderImpl implements CommandLineArgumentsReader {

    private JsonConfigValidator jsonConfigValidator;
    private JsonReader jsonReader;

    public CommandLineArgumentsReaderImpl(JsonConfigValidator jsonConfigValidator, JsonReader jsonReader) {
        this.jsonConfigValidator = jsonConfigValidator;
        this.jsonReader = jsonReader;
    }

    /**
     * If the c command line option is given, it's user config file will be read.
     * The user config file overwrites values from the default config file.
     * Other command line options overwrite config values from both config files.
     * @param rawInput A {@link RawInput} instance. Configuration is read into this instance.
     * @param commandLine An instance of type {@link CommandLine} which contains command line options.
     */
    public void read(RawInput rawInput, CommandLine commandLine) {
        if(commandLine.hasOption("c")) {
            Path userConfigPath = Paths.get(commandLine.getOptionValue("c"));

            /*
             * An anonymous subclass is created which reads the user config file
             * from the given path.
             */
            ConfigFileReader configFileReader = new ConfigFileReader(jsonConfigValidator,jsonReader) {
                @Override
                protected Path createConfigFilePath() {
                    return userConfigPath;
                }
            };

            //the file must exist in this case
            configFileReader.readMandatoryConfigFile(rawInput);
        }

        if(commandLine.hasOption("w")) {
            rawInput.setWebsocketUri(commandLine.getOptionValue("w"));
        }
    }
}
