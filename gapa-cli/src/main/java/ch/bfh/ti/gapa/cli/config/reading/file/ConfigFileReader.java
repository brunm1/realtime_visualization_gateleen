package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Reads a configuration from a valid json file.
 * This is an abstract class. Subclasses overwrite {@link #createConfigFilePath()}
 * which creates the path to the config file.
 */
public abstract class ConfigFileReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigFileReader.class.getName());
    private static final String CONFIG_FILE_NOT_FOUND = "Config file not found: ";
    private static final String COULD_NOT_BUILD_PATH_TO_CONFIG_FILE = "Could not build path to config file.";
    private static final String COULD_NOT_OPEN_CONFIG_FILE_INPUT_STREAM = "Could not open config file input stream.";

    private JsonConfigValidator configFileValidator;
    private JsonReader jsonReader;

    /**
     *
     * @param jsonConfigValidator validates config file data
     * @param jsonReader reads config data into {@link RawInput}
     */
    protected ConfigFileReader(JsonConfigValidator jsonConfigValidator, JsonReader jsonReader) {
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
     * Reads configuration from valid config file.
     * Logs a message if the config file does not exist.
     * @param rawInput Data is read into this instance.
     */
    void readOptionalConfigFile(RawInput rawInput) {
        readConfigFile(rawInput, true);
    }

    /**
     * Reads configuration from valid config file.
     * Throws if config file does not exist.
     * @param rawInput Data is read into this instance.
     * @throws IllegalArgumentException if config file does not exist
     */
    public void readMandatoryConfigFile(RawInput rawInput) {
        readConfigFile(rawInput, false);
    }

    private String createConfigNotFoundMessage(String path) {
        return CONFIG_FILE_NOT_FOUND +path;
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
            throw new RuntimeException(COULD_NOT_BUILD_PATH_TO_CONFIG_FILE, e);
        }
    }

    private InputStream getConfigFileInputStream(Path configFilePath) {
        try {
            return Files.newInputStream(configFilePath);
        } catch (IOException e) {
            throw new RuntimeException(COULD_NOT_OPEN_CONFIG_FILE_INPUT_STREAM, e);
        }
    }
}
