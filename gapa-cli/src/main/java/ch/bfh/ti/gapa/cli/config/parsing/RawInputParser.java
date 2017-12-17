package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;

/**
 * Parses fields from a {@link RawInput} instance and writes the
 * results into the given {@link Input} instance
 */
@FunctionalInterface
public interface RawInputParser {

    /**
     * Parses data from {@link RawInput} and saves it in {@link Input}
     * @param rawInput Unparsed data collected from different sources
     * @param input Target {@link Input} instance that will hold the parsed data
     * @throws RawInputParseException when a {@link ch.bfh.ti.gapa.cli.config.ConfigField} cannot be parsed
     */
    void parse(RawInput rawInput, Input input) throws RawInputParseException;
}
