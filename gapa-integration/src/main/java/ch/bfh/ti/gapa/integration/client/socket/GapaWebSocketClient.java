package ch.bfh.ti.gapa.integration.client.socket;

import ch.bfh.ti.gapa.integration.client.Client;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class GapaWebSocketClient extends WebSocketClient implements Client {
    private StringReceiver stringReceiver;

    public GapaWebSocketClient(URI serverUri, StringReceiver stringReceiver) {
        super(serverUri);
        this.stringReceiver = stringReceiver;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(String s) {
        stringReceiver.receiveString(s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
