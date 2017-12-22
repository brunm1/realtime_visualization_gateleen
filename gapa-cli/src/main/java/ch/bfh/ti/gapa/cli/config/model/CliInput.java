package ch.bfh.ti.gapa.cli.config.model;

import org.json.JSONArray;

/**
 * Contains user input. Either from cli arguments
 * or config files.
 */
public class CliInput {
    private String websocketUri;
    private JSONArray filters;
    private String serverName;

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

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
