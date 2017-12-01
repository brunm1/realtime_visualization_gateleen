package ch.bfh.ti.gapa.process.interfaces;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Input {
    private Pattern inboundRequestPattern;
    private Pattern outboundRequestPattern;
    private DateTimeFormatter dateTimeFormatter;
    private InputStream inputStream;
    private URI websocketUri;
    private JSONArray filters;

    public Pattern getInboundRequestPattern() {
        return inboundRequestPattern;
    }

    public void setInboundRequestPattern(Pattern inboundRequestPattern) {
        this.inboundRequestPattern = inboundRequestPattern;
    }

    public Pattern getOutboundRequestPattern() {
        return outboundRequestPattern;
    }

    public void setOutboundRequestPattern(Pattern outboundRequestPattern) {
        this.outboundRequestPattern = outboundRequestPattern;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
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
}
