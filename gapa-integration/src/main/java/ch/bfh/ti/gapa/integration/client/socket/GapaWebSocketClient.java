package ch.bfh.ti.gapa.integration.client.socket;

import ch.bfh.ti.gapa.integration.client.Client;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GapaWebSocketClient extends WebSocketClient implements Client {
    private static final Logger LOGGER = Logger.getLogger(GapaWebSocketClient.class.getName());

    private StringReceiver stringReceiver;
    private GapaWebSocketClientHandler gapaWebSocketClientHandler;

    public GapaWebSocketClient(URI serverUri, StringReceiver stringReceiver, GapaWebSocketClientHandler gapaWebSocketClientHandler) {
        super(serverUri);
        this.stringReceiver = stringReceiver;
        this.gapaWebSocketClientHandler = gapaWebSocketClientHandler;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Opened connection");
    }

    @Override
    public void onMessage(String s) {
        LOGGER.log(Level.FINE, "Received message:" + System.lineSeparator() + s);
        stringReceiver.receiveString(s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codes are documented in class org.java_websocket.framing.CloseFrame
        gapaWebSocketClientHandler.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        gapaWebSocketClientHandler.onError(ex);
    }
}
