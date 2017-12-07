package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;

/**
 * Reads the default configuration from the
 * default config file.
 */
@FunctionalInterface
public interface DefaultConfigFileReader {
    /**
     * Reads config from the default file.
     * @param rawInput where the read data is written to
     */
    void read(RawInput rawInput);
}
