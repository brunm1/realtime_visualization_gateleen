package ch.bfh.ti.gapa.cli.config.reading.commandline;

import ch.bfh.ti.gapa.cli.config.model.CliInput;
import org.apache.commons.cli.CommandLine;

/**
 * Reads configuration from command line arguments.
 */
@FunctionalInterface
public interface CommandLineArgumentsReader {
    /**
     * Reads command line arguments from {@link CommandLine}
     * and writes them into {@link CliInput}.
     * @param cliInput Read data is written into this instance.
     * @param commandLine Contains command line arguments
     */
    void read(CliInput cliInput, CommandLine commandLine);
}
