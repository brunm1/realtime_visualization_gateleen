package ch.bfh.ti.gapa.domain.recording;

import ch.bfh.ti.gapa.process.recording.ListenerHooks;
import org.junit.jupiter.api.Test;

public class ListenerHooksTest {
    @Test
    public void testJsonImport() {
        String json = "{\n" +
                "  \"requesturl\": \"/gateleen/services/ts/_hooks/route\",\n" +
                "  \"expirationTime\": \"2017-11-02T11:09:44.300\",\n" +
                "  \"hook\": {\n" +
                "    \"destination\": \"http://localhost:8080/ts\",\n" +
                "    \"methods\": []\n" +
                "  }\n" +
                "}";

        ListenerHooks.readJson(json);
    }
}
