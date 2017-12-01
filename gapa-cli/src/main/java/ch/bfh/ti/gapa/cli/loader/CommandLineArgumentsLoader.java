package ch.bfh.ti.gapa.cli.loader;

import ch.bfh.ti.gapa.cli.parsing.ParseException;
import ch.bfh.ti.gapa.process.interfaces.Input;
import org.apache.commons.cli.CommandLine;

public interface CommandLineArgumentsLoader {
    void loadInput(Input input, CommandLine commandLine) throws ParseException;
}
