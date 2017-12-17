package ch.bfh.ti.gapa.process.interfaces;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ProcessLayerInput {
    private URI websocketUri;
    private JSONArray filters;
    private String serverName;

    /**
     * Sets input defaults
     */
    public ProcessLayerInput(){
        try {
            websocketUri = new URI("http://localhost:7012");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        serverName = "gateleen";
    }

    public URI getWebsocketUri() {
        return websocketUri;
    }

    public void setWebsocketUri(URI websocketUri) {
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
