package ch.bfh.ti.gapa.cli.config.reading.file.json;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import org.json.JSONObject;

/**
 * Reads the data from a json object into
 * an {@link RawInput} instance.
 */
public class JsonReaderImpl implements JsonReader {

    /**
     * Creates an instance of {@link RawInput} and copies data from a {@link JSONObject} to this instance.
     * The websocketUri is read as string.
     * The filters are optional and are read as {@link org.json.JSONArray}.
     */
    public void read(RawInput rawInput, JSONObject jsonObject) {
        rawInput.setWebsocketUri(jsonObject.getString(ConfigJsonField.websocketUri.name()));
        rawInput.setFilters(jsonObject.optJSONArray(ConfigJsonField.filters.name()));
    }
}
