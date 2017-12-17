package ch.bfh.ti.gapa.process.interfaces;

public interface ProcessLayerHandler {
    void result(String plantUml);

    void onWebSocketError(Exception ex);

}
