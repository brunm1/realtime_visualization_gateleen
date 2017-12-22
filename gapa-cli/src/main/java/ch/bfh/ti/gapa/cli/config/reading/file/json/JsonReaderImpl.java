package ch.bfh.ti.gapa.cli.config.reading.file.json;

import ch.bfh.ti.gapa.cli.config.model.ConfigField;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import org.json.JSONObject;

/**
 * Reads the data from a json object into
 * an {@link CliInput} instance.
 */
public class JsonReaderImpl implements JsonReader {
    /**
     * Creates an instance of {@link CliInput} and copies data from a {@link JSONObject} to this instance.
     * The websocketUri and serverName are read as string.
     * The filters are read as {@link org.json.JSONArray}.
     * All fields are optional.
     */
    public void read(CliInput cliInput, JSONObject jsonObject) {
        cliInput.setWebsocketUri(jsonObject.optString(ConfigField.websocketUri.name()));
        cliInput.setFilters(jsonObject.optJSONArray(ConfigField.filters.name()));
        cliInput.setServerName(jsonObject.optString(ConfigField.serverName.name()));
    }
}
