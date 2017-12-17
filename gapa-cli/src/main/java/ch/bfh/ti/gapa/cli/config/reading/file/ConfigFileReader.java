package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.config.model.CliInput;

import java.nio.file.Path;

public interface ConfigFileReader {
    void readConfigFile(CliInput cliInput);

    void readConfigFile(CliInput cliInput, Path configFilePath);
}
