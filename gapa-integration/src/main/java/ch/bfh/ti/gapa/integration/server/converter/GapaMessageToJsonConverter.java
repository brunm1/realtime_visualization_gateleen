package ch.bfh.ti.gapa.integration.server.converter;

import ch.bfh.ti.gapa.integration.converter.GapaMessageJsonField;
import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.integration.server.Server;
import org.json.JSONException;
import org.json.JSONObject;

public class GapaMessageToJsonConverter implements Server{
    private JsonSender jsonSender;

    public GapaMessageToJsonConverter(JsonSender jsonSender) {
        this.jsonSender = jsonSender;
    }

    public void sendGapaMessage(GapaMessage m) throws JSONException {
        JSONObject o = new JSONObject();
        o.put(GapaMessageJsonField.timestamp.name(), m.getTimestamp().toString());
        o.put(GapaMessageJsonField.method.name(), m.getMethod().name());
        o.put(GapaMessageJsonField.path.name(), m.getPath());
        o.put(GapaMessageJsonField.type.name(), m.getType().name());
        o.put(GapaMessageJsonField.peer.name(), m.getPeer());
        o.put(GapaMessageJsonField.traceId.name(), m.getTraceId());
        jsonSender.sendJson(o);
    }
}
