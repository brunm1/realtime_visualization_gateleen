package ch.bfh.ti.gapa.integration.client.converter;

import ch.bfh.ti.gapa.integration.model.GapaMessage;

public interface GapaMessageReceiver {
    void receiveMessage(GapaMessage m);
}
