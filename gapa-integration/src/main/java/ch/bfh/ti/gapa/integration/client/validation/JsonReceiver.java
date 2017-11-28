package ch.bfh.ti.gapa.integration.client.validation;

import org.json.JSONObject;

public interface JsonReceiver {
    void receiveJson(JSONObject jsonObject);
}
