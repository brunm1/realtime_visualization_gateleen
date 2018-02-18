package ch.bfh.ti.gapa.process.recording;

import ch.bfh.ti.gapa.process.recording.ListenerHooks;
import org.junit.jupiter.api.Test;

public class ListenerHooksTest {
    @Test
    public void testJsonImport() {
        String json = "{" +
                "  \"requesturl\": \"/gateleen/services/ts/_hooks/route\"," +
                "  \"expirationTime\": \"2017-11-02T11:09:44.300\"," +
                "  \"hook\": {" +
                "    \"destination\": \"http://localhost:8080/ts\"," +
                "    \"methods\": []" +
                "  }" +
                "}";

        ListenerHooks.readJson(json);
    }
}
