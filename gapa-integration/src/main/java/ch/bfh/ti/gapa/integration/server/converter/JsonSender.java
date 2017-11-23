package ch.bfh.ti.gapa.integration.server.converter;

import org.json.JSONObject;

public interface JsonSender {
    void sendJson(JSONObject o);
}
