package ch.bfh.ti.gapa.integration.client.socket;

public interface GapaWebSocketClientHandler {
    void onError(Exception ex);
    void onClose(int code, String reason, boolean remote);
}
