package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.ConfigField;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Parses data in {@link RawInput} and writes the result into the given
 * {@link Input} instance.
 */
public class RawInputParserImpl implements RawInputParser {
    private static URI convertStringToUri(String s) throws URISyntaxException {
        return new URI(s);
    }

    /**
     * Parses configuration values if necessary and writes them into {@link Input}.
     * @param rawInput Unparsed data collected from different sources
     * @param input Target {@link Input} instance that will hold the parsed data
     * @throws RawInputParseException when a {@link ConfigField} cannot be parsed
     */
    @Override
    public void parse(RawInput rawInput, Input input) throws RawInputParseException {
        try {
            input.setWebsocketUri(convertStringToUri(rawInput.getWebsocketUri()));
        } catch (URISyntaxException e) {
            throw new RawInputParseException(ConfigField.websocketUri, e);
        }

        input.setServerName(rawInput.getServerName());
        input.setFilters(rawInput.getFilters());
    }
}
