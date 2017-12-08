package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.CliImpl;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Locates the default config file in the directory
 * where the executed jar is and reads it.
 */
public class DefaultConfigFileReaderImpl extends ConfigFileReader implements DefaultConfigFileReader {

    /**
     * @param configFileValidator validates the default config file.
     * @param jsonReader reads from the default config file
     */
    public DefaultConfigFileReaderImpl(JsonConfigValidator configFileValidator, JsonReader jsonReader) {
        super(configFileValidator, jsonReader);
    }

    /**
     *
     * @return the path to the config.json file in the same directory as the executed jar.
     * @throws URISyntaxException when the path to executed jar cannot be built.
     */
    @Override
    protected Path createConfigFilePath() throws URISyntaxException {
        return getPathToExecutedJar().getParent().resolve("config.json");
    }

    private static Path getPathToExecutedJar() throws URISyntaxException {
        return Paths.get(CliImpl.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    /**
     * Reads the default configuration into {@link RawInput}
     * @param rawInput where the read data is written to
     */
    @Override
    public void read(RawInput rawInput) {
        readOptionalConfigFile(rawInput);
    }
}
