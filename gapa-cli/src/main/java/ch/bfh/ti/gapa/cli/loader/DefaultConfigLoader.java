package ch.bfh.ti.gapa.cli.loader;

import ch.bfh.ti.gapa.cli.parsing.ParseException;
import ch.bfh.ti.gapa.process.interfaces.Input;

@FunctionalInterface
public interface DefaultConfigLoader {
    void loadInput(Input input) throws ParseException;
}
