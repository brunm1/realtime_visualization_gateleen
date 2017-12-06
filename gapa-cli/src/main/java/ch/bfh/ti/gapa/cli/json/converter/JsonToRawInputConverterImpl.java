package ch.bfh.ti.gapa.cli.json.converter;

import ch.bfh.ti.gapa.cli.raw.RawInput;
import org.json.JSONObject;

public class JsonToRawInputConverterImpl implements JsonToRawInputConverter {

    /**
     * Creates an instance of RawInput and copies data from a JSONObject to this instance.
     * The websocketUri is read as string.
     * The filters are optional and are read as JSONArray.
     */
    public RawInput convert(JSONObject jsonObject) {
        RawInput rawInput = new RawInput();
        rawInput.setWebsocketUri(jsonObject.getString(ConfigJsonField.websocketUri.name()));
        rawInput.setFilters(jsonObject.optJSONArray(ConfigJsonField.filters.name()));
        return rawInput;
    }
}
