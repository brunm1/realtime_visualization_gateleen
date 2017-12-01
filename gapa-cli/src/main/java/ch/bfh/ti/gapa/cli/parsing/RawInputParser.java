package ch.bfh.ti.gapa.cli.parsing;

import ch.bfh.ti.gapa.cli.raw.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;

public interface RawInputParser {
    void parse(RawInput rawInput, Input input) throws ParseException;
}
