package ch.bfh.ti.gapa.playground;

import ch.bfh.ti.gapa.integration.client.Client;
import ch.bfh.ti.gapa.integration.client.converter.GapaMessageReceiver;
import ch.bfh.ti.gapa.integration.client.converter.JsonToGapaMessageConverter;
import ch.bfh.ti.gapa.integration.client.socket.GapaWebSocketClient;
import ch.bfh.ti.gapa.integration.client.socket.GapaWebSocketClientHandler;
import ch.bfh.ti.gapa.integration.client.socket.StringReceiver;
import ch.bfh.ti.gapa.integration.client.validation.GapaMessageJsonValidator;
import ch.bfh.ti.gapa.integration.client.validation.JsonReceiver;

import java.net.URI;
import java.net.URISyntaxException;

public class GapaMessageReceiverImpl {
    public static void main(String[] args) {
        try {
            GapaMessageReceiver gapaMessageReceiver =
                    gapaMessage -> {
                        System.out.println("Gapa received: " + gapaMessage.toString());
                        System.exit(0);
                    };
            JsonReceiver jsonReceiver = new JsonToGapaMessageConverter(gapaMessageReceiver);
            StringReceiver stringReceiver = new GapaMessageJsonValidator(jsonReceiver);
            Client gapaWebsocketClient =
                    new GapaWebSocketClient(new URI("ws://localhost:7012"), stringReceiver, new GapaWebSocketClientHandler() {
                        @Override
                        public void onError(Exception ex) {

                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {

                        }
                    });
            gapaWebsocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
