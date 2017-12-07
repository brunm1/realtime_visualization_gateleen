package ch.bfh.ti.gapa.cli.config.reading.model;

import org.json.JSONArray;

/**
 * Read configuration data is saved here.
 * Is used as an intermediary type so that
 * the config data can be overwritten if several
 * config sources are used.
 */
public class RawInput {
    private String websocketUri;
    private JSONArray filters;

    public String getWebsocketUri() {
        return websocketUri;
    }

    public void setWebsocketUri(String websocketUri) {
        this.websocketUri = websocketUri;
    }

    public JSONArray getFilters() {
        return filters;
    }

    public void setFilters(JSONArray filters) {
        this.filters = filters;
    }
}
