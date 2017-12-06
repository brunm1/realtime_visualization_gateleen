package ch.bfh.ti.gapa.cli.config.reading.commandline;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import org.apache.commons.cli.CommandLine;

public interface CommandLineArgumentsReader {
    void read(RawInput input, CommandLine commandLine);
}
