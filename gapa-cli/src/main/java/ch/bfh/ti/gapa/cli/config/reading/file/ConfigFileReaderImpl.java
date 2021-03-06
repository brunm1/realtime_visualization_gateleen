package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.CliImpl;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReaderImpl;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidatorImpl;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads config from a valid json file.
 */
public class ConfigFileReaderImpl implements ConfigFileReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigFileReaderImpl.class.getName());
    private static final String CONFIG_FILE_NOT_FOUND = "Config file not found: ";
    private static final String COULD_NOT_BUILD_PATH_TO_CONFIG_FILE = "Could not build path to config file.";
    private static final String COULD_NOT_OPEN_CONFIG_FILE_INPUT_STREAM = "Could not open config file input stream.";
    private static final String CONFIG_JSON = "config.json";

    private JsonConfigValidator configFileValidator;
    private JsonReader jsonReader;

    /**
     *
     * @param jsonConfigValidator validates config file data
     * @param jsonReader reads config data into {@link CliInput}
     */
    public ConfigFileReaderImpl(JsonConfigValidator jsonConfigValidator, JsonReader jsonReader) {
        this.configFileValidator = jsonConfigValidator;
        this.jsonReader = jsonReader;
    }

    /**
     * Creates an instance with default dependencies.
     */
    public ConfigFileReaderImpl() {
        this.configFileValidator = new JsonConfigValidatorImpl();
        this.jsonReader = new JsonReaderImpl();
    }

    /**
     * @return the path to the config file in the same directory as the executed jar.
     */
    private Path createDefaultConfigFilePath() {
        try {
            return getPathToExecutedJar().getParent().resolve(CONFIG_JSON);
        } catch (URISyntaxException e) {
            throw new RuntimeException(COULD_NOT_BUILD_PATH_TO_CONFIG_FILE, e);
        }
    }

    private static Path getPathToExecutedJar() throws URISyntaxException {
        return Paths.get(CliImpl.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    /**
     * Reads configuration from valid config file in same directory.
     * Logs a message if the config file does not exist.
     * @param cliInput Data is read into this instance.
     */
    @Override
    public void readConfigFile(CliInput cliInput){
        readConfigFile(cliInput, true, createDefaultConfigFilePath());
    }

    /**
     * Reads configuration from valid config file.
     * Throws if config file does not exist.
     * @param cliInput Data is read into this instance.
     * @throws IllegalArgumentException if config file does not exist
     */
    @Override
    public void readConfigFile(CliInput cliInput, Path configFilePath) {
        readConfigFile(cliInput, false, configFilePath);
    }

    private void readConfigFile(CliInput cliInput, boolean optional, Path configFilePath) {
        if(Files.exists(configFilePath)) {
            InputStream inputStream = getConfigFileInputStream(configFilePath);

            //read and validate config file
            JSONObject jsonObject = configFileValidator.validate(inputStream);

            //Read from json object into cliInput
            jsonReader.read(cliInput, jsonObject);
        } else {
            String path = configFilePath.toString();
            if(optional) {
                logNonExistingConfigFile(path);
            } else {
                throwNonExistingConfigFile(path);
            }
        }
    }

    private String createConfigNotFoundMessage(String path) {
        return CONFIG_FILE_NOT_FOUND +path;
    }

    private void logNonExistingConfigFile(String path) {
        LOGGER.log(Level.CONFIG,createConfigNotFoundMessage(path));
    }

    private void throwNonExistingConfigFile(String path) {
        throw new IllegalArgumentException(createConfigNotFoundMessage(path));
    }

    private InputStream getConfigFileInputStream(Path configFilePath) {
        try {
            return Files.newInputStream(configFilePath);
        } catch (IOException e) {
            throw new RuntimeException(COULD_NOT_OPEN_CONFIG_FILE_INPUT_STREAM, e);
        }
    }
}
