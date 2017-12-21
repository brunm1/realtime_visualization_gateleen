package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.model.ConfigField;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import ch.bfh.ti.gapa.process.interfaces.ProcessLayerInput;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Parses data in {@link CliInput} and writes the result into the given
 * {@link ProcessLayerInput} instance.
 */
public class CliInputParserImpl implements CliInputParser {
    private static URI convertStringToUri(String s) throws URISyntaxException {
        return new URI(s);
    }

    /**
     * Parses configuration values if necessary and writes them into {@link ProcessLayerInput}.
     * @param cliInput Unparsed data collected from cli arguments and config files
     * @param processLayerInput Target {@link ProcessLayerInput} instance that will hold the parsed data
     * @throws CliInputParseException when a {@link ConfigField} cannot be parsed
     */
    @Override
    public void parse(CliInput cliInput, ProcessLayerInput processLayerInput) throws CliInputParseException {
        try {
            processLayerInput.setWebsocketUri(convertStringToUri(cliInput.getWebsocketUri()));
        } catch (URISyntaxException e) {
            throw new CliInputParseException(ConfigField.websocketUri, e);
        }

        processLayerInput.setServerName(cliInput.getServerName());
        processLayerInput.setFilters(cliInput.getFilters());
    }
}
