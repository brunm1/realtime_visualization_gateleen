package ch.bfh.ti.gapa.cli.json.converter;

import ch.bfh.ti.gapa.cli.parsing.RawInputField;
import ch.bfh.ti.gapa.cli.raw.RawInput;
import org.json.JSONObject;

public class JsonToRawInputConverterImpl implements JsonToRawInputConverter {
    public RawInput convert(JSONObject jsonObject) {
        RawInput rawInput = new RawInput();
        rawInput.setWebsocketUri(jsonObject.getString(ConfigJsonField.websocketUri.name()));
        rawInput.setFilters(jsonObject.optJSONArray(RawInputField.filters.name()));
        return rawInput;
    }
}
