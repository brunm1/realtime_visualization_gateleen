package ch.bfh.ti.gapa.cli.reading.file;

import ch.bfh.ti.gapa.cli.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.raw.RawInput;
import ch.bfh.ti.gapa.cli.reading.file.json.validation.JsonConfigValidator;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Reads a configuration from a valid json file.
 * This is an abstract class. Subclasses overwrite createConfigFilePath
 * which creates the path to the config file.
 */
public abstract class ConfigFileReader {
    private final static Logger LOGGER = Logger.getLogger(ConfigFileReader.class.getName());
    private final static String notFoundText = "Config file not found: ";

    private JsonConfigValidator configFileValidator;
    private JsonReader jsonReader;

    public ConfigFileReader(JsonConfigValidator jsonConfigValidator, JsonReader jsonReader) {
        this.configFileValidator = jsonConfigValidator;
        this.jsonReader = jsonReader;
    }

    private void readConfigFile(RawInput rawInput, boolean optional) {
        Path configFilePath = getConfigFilePath();

        if(Files.exists(configFilePath)) {
            InputStream inputStream = getConfigFileInputStream(configFilePath);

            //read and validate config file
            JSONObject jsonObject = configFileValidator.validate(inputStream);

            //Read from json object into rawInput
            jsonReader.read(rawInput, jsonObject);
        } else {
            String path = configFilePath.toString();
            if(optional) {
                logNonExistingConfigFile(path);
            } else {
                throwNonExistingConfigFile(path);
            }
        }
    }

    /**
     * Logs a message if the config file does not exist.
     * @param rawInput Data is read into this instance.
     */
    public void readOptionalConfigFile(RawInput rawInput) {
        readConfigFile(rawInput, true);
    }

    /**
     * Throws if config file does not exist.
     * @param rawInput Data is read into this instance.
     * @throws IllegalArgumentException if config file does not exist
     */
    public void readMandatoryConfigFile(RawInput rawInput) {
        readConfigFile(rawInput, false);
    }

    private String createConfigNotFoundMessage(String path) {
        return notFoundText+path;
    }

    private void logNonExistingConfigFile(String path) {
        LOGGER.info(createConfigNotFoundMessage(path));
    }

    private void throwNonExistingConfigFile(String path) {
        throw new IllegalArgumentException(createConfigNotFoundMessage(path));
    }

    /**
     * Will be overwritten by subclasses
     * @return The path to a config file
     * @throws URISyntaxException if the path could not be built.
     */
    protected abstract Path createConfigFilePath() throws URISyntaxException;

    private Path getConfigFilePath() {
        try {
            return createConfigFilePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not build path to config file.", e);
        }
    }

    private InputStream getConfigFileInputStream(Path configFilePath) {
        try {
            return Files.newInputStream(configFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not open default config file input stream.", e);
        }
    }
}
