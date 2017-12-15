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

    public GapaWebSocketClient(URI serverUri, StringReceiver stringReceiver) {
        super(serverUri);
        this.stringReceiver = stringReceiver;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Opened connection");
    }

    @Override
    public void onMessage(String s) {
        stringReceiver.receiveString(s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codes are documented in class org.java_websocket.framing.CloseFrame
        LOGGER.severe("Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError(Exception ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
    }
}
