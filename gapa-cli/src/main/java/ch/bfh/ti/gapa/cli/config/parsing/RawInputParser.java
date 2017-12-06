package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;

public interface RawInputParser {
    void parse(RawInput rawInput, Input input) throws ParseException;
}
