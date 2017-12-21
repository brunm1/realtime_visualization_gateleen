package ch.bfh.ti.gapa.cli.printer;

import ch.bfh.ti.gapa.cli.exception.CommandLineException;

import java.io.IOException;

public interface InfoPrinter {
    void printHelp();

    void printVersion() throws IOException;

    void printCommandLineException(CommandLineException e);
}
