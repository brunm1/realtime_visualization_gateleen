package ch.bfh.ti.gapa.cli.printer;

import java.io.IOException;

/**
 * Prints information about the Gapa application.
 */
public interface GapaInfoPrinter extends InfoPrinter {
    /**
     * Prints the json schema for gapa configurations.
     * @throws IOException if schema could not be loaded.
     */
    void printConfigSchema() throws IOException;
}
