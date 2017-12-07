package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import ch.bfh.ti.gapa.cli.config.reading.model.RawInputField;
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
     * If a field in {@link RawInput} is not null, it is parsed and
     * saved in {@link Input}. Existing values in {@link Input} are
     * overwritten.
     * @param rawInput Unparsed data collected from different sources
     * @param input Target {@link Input} instance that will hold the parsed data
     * @throws RawInputParseException when a {@link ch.bfh.ti.gapa.cli.config.reading.model.RawInputField} cannot be parsed
     */
    @Override
    public void parse(RawInput rawInput, Input input) throws RawInputParseException {
        if(rawInput.getWebsocketUri() != null) {
            try {
                URI websocketUri = convertStringToUri(rawInput.getWebsocketUri());
                input.setWebsocketUri(websocketUri);
            } catch (URISyntaxException e) {
                throw new RawInputParseException(RawInputField.websocketUri, e);
            }
        }

        if(rawInput.getFilters() != null) {
            //was already validated, no further conversions needed
            //filters are kept dynamic for maximum flexibility
            //more validation on filters is done in the process layer
            input.setFilters(rawInput.getFilters());
        }
    }
}
