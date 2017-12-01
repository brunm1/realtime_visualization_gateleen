package ch.bfh.ti.gapa.cli.raw;

import org.json.JSONArray;

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
