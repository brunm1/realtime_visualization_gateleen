package ch.bfh.ti.gapa.cli.config.reading.file;

import ch.bfh.ti.gapa.cli.config.model.CliInput;

import java.nio.file.Path;

/**
 * Reads config from a file into {@link CliInput}.
 */
public interface ConfigFileReader {

    /**
     * Reads config from a file at a default location.
     * @param cliInput Data is read into this instance.
     */
    void readConfigFile(CliInput cliInput) throws Throwable;

    /**
     * Reads config from a file at the given {@link Path}.
     * @param cliInput Data is read into this instance.
     */
    void readConfigFile(CliInput cliInput, Path configFilePath) throws Throwable;
}
