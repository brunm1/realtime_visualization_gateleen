package ch.bfh.ti.gapa.cli.printer;

import ch.bfh.ti.gapa.cli.exception.CommandLineException;

import java.io.IOException;

/**
 * Prints information about the application.
 */
public interface InfoPrinter {
    /**
     * Prints out how the CLI application can be used.
     */
    void printHelp();

    /**
     * Prints the application version.
     */
    void printVersion() throws IOException;

    /**
     * Prints out a user friendly message about an
     * exception that stopped the application from
     * working.
     * @param commandLineException A general exception with a description that wraps the underlying exception.
     */
    void printCommandLineException(CommandLineException commandLineException);
}
