package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.Cli;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.cli.config.reading.file.json.JsonReader;
import ch.bfh.ti.gapa.cli.config.reading.file.json.validation.JsonConfigValidator;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultConfigFileReaderImpl extends ConfigFileReader implements DefaultConfigFileReader {

    public DefaultConfigFileReaderImpl(JsonConfigValidator configFileValidator, JsonReader jsonReader) {
        super(configFileValidator, jsonReader);
    }

    @Override
    protected Path createConfigFilePath() throws URISyntaxException {
        return getPathToExecutedJar().getParent().resolve("config.json");
    }

    private static Path getPathToExecutedJar() throws URISyntaxException {
        return Paths.get(Cli.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    @Override
    public void read(RawInput rawInput) {
        readOptionalConfigFile(rawInput);
    }
}
