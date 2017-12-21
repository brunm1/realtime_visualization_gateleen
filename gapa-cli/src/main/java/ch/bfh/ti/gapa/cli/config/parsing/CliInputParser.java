package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.cli.config.model.ConfigField;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;

/**
 * Parses fields from a {@link CliInput} instance and writes the
 * results into the given {@link ProcessLayerInput} instance
 */
@FunctionalInterface
public interface CliInputParser {

    /**
     * Parses data from {@link CliInput} and saves it in {@link ProcessLayerInput}
     * @param cliInput Unparsed data collected from cli arguments or config files
     * @param processLayerInput Target {@link ProcessLayerInput} instance that will hold the parsed data
     * @throws CliInputParseException when a {@link ConfigField} cannot be parsed
     */
    void parse(CliInput cliInput, ProcessLayerInput processLayerInput) throws CliInputParseException;
}
