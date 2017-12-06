package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;

@FunctionalInterface
public interface DefaultConfigFileReader {
    void read(RawInput input);
}
