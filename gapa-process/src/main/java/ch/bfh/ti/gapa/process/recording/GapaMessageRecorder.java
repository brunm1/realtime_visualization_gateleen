package ch.bfh.ti.gapa.process.recording;

import ch.bfh.ti.gapa.integration.client.converter.GapaMessageReceiver;
import ch.bfh.ti.gapa.integration.model.GapaMessage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GapaMessageRecorder implements GapaMessageReceiver {
    private Queue<GapaMessage> gapaMessages = new ConcurrentLinkedQueue<>();

    public void reset() {
        gapaMessages.clear();
    }

    public Queue<GapaMessage> getGapaMessages() {
        return gapaMessages;
    }

    @Override
    public void receiveMessage(GapaMessage m) {
        gapaMessages.add(m);
    }
}
