package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;

import java.nio.file.Path;

public interface ConfigFileReader {
    void readConfigFile(RawInput rawInput);

    void readConfigFile(RawInput rawInput, Path configFilePath);
}
