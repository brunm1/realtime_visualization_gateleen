package ch.bfh.ti.gapa.cli.parsing;

import ch.bfh.ti.gapa.cli.raw.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;

import java.net.URI;
import java.net.URISyntaxException;

public class RawInputParserImpl implements RawInputParser {
    private static URI convertStringToUri(String s) throws URISyntaxException {
        return new URI(s);
    }

    @Override
    public void parse(RawInput rawInput, Input input) throws ParseException {
        if(rawInput.getWebsocketUri() != null) {
            try {
                URI websocketUri = convertStringToUri(rawInput.getWebsocketUri());
                input.setWebsocketUri(websocketUri);
            } catch (URISyntaxException e) {
                throw new ParseException(RawInputField.websocketUri, e);
            }
        }

        if(rawInput.getFilters() != null) {
            //was already validated, no further conversions needed
            //filters are kept dynamic for maximum flexibility
            input.setFilters(rawInput.getFilters());
        }
    }
}
