package ch.bfh.ti.gapa.integration.client.converter;

import ch.bfh.ti.gapa.integration.client.validation.JsonReceiver;
import ch.bfh.ti.gapa.integration.converter.GapaMessageJsonField;
import ch.bfh.ti.gapa.integration.model.GapaMessage;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Instant;

public class JsonToGapaMessageConverter implements JsonReceiver{
    private GapaMessageReceiver gapaMessageReceiver;

    public JsonToGapaMessageConverter(GapaMessageReceiver gapaMessageReceiver) {
        this.gapaMessageReceiver = gapaMessageReceiver;
    }

    public void receiveJson(JSONObject o) throws JSONException {
        GapaMessage m = new GapaMessage();
        m.setTimestamp(Instant.parse(o.getString(GapaMessageJsonField.timestamp.name())));
        m.setMethod(GapaMessage.Method.valueOf(o.getString(GapaMessageJsonField.method.name())));
        m.setPath(o.getString(GapaMessageJsonField.path.name()));
        m.setType(GapaMessage.Type.valueOf(o.getString(GapaMessageJsonField.type.name())));
        m.setPeer(o.getString(GapaMessageJsonField.peer.name()));
        m.setTraceId(o.optString(GapaMessageJsonField.traceId.name()));
        gapaMessageReceiver.receiveMessage(m);
    }
}
