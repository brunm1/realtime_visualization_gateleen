package ch.bfh.ti.gapa.integration.server;

import ch.bfh.ti.gapa.integration.model.GapaMessage;

public interface Server {
    void sendGapaMessage(GapaMessage m);
}
