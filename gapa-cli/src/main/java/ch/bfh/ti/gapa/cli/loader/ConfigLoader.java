package ch.bfh.ti.gapa.cli.loader;

import ch.bfh.ti.gapa.cli.json.converter.JsonToRawInputConverter;
import ch.bfh.ti.gapa.cli.json.validation.ConfigFileValidator;
import ch.bfh.ti.gapa.cli.parsing.ParseException;
import ch.bfh.ti.gapa.cli.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.raw.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public abstract class ConfigLoader {
    private final static Logger LOGGER = Logger.getLogger(ConfigLoader.class.getName());
    private final static String notFoundText = "Config file not found: ";

    private ConfigFileValidator configFileValidator;
    private JsonToRawInputConverter jsonToRawInputConverter;
    private RawInputParser rawInputParser;

    ConfigLoader(ConfigFileValidator configFileValidator,
                 JsonToRawInputConverter jsonToRawInputConverter,
                 RawInputParser rawInputParser) {
        this.configFileValidator = configFileValidator;
        this.jsonToRawInputConverter = jsonToRawInputConverter;
        this.rawInputParser = rawInputParser;
    }

    protected void loadInput(Input input, boolean optional) throws ParseException {
        Path configFilePath = getConfigFilePath();

        if(Files.exists(configFilePath)) {
            //load and validate config file
            JSONObject jsonObject = configFileValidator.validate(getDefaultConfigFileInputStream());

            //Input to middle format
            RawInput rawInput = jsonToRawInputConverter.convert(jsonObject);

            //Parse strings in RawInput and set attributes in Input
            rawInputParser.parse(rawInput, input);
        } else {
            //ignore or throw depending on optional boolean
            String message = notFoundText+configFilePath.toString();
            if(optional) {
                LOGGER.info(message);
            } else {
                throw new IllegalArgumentException(message);
            }
        }
    }

    abstract Path createConfigFilePath() throws URISyntaxException;

    private Path getConfigFilePath() {
        try {
            return createConfigFilePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not build path to config file.", e);
        }
    }

    private InputStream getDefaultConfigFileInputStream() {
        try {
            return Files.newInputStream(getConfigFilePath());
        } catch (IOException e) {
            throw new RuntimeException("Could not open default config file input stream.", e);
        }
    }
}
