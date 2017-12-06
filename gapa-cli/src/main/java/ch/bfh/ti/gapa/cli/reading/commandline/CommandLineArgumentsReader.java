package ch.bfh.ti.gapa.cli.reading.commandline;

import ch.bfh.ti.gapa.cli.raw.RawInput;
import org.apache.commons.cli.CommandLine;

public interface CommandLineArgumentsReader {
    void read(RawInput input, CommandLine commandLine);
}
