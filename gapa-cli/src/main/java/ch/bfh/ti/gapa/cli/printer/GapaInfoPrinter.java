package ch.bfh.ti.gapa.cli.printer;

import java.io.IOException;

public interface GapaInfoPrinter extends InfoPrinter {
    void printConfigSchema() throws IOException;
}
