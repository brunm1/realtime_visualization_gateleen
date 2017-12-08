package ch.bfh.ti.gapa.process.diagram;

import ch.bfh.ti.gapa.domain.recording.Record;

public class SequenceDiagramMessage {
    private String from;
    private String to;
    private String label;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static SequenceDiagramMessage createFromRecord(Record record) {
        SequenceDiagramMessage sequenceDiagramMessage = new SequenceDiagramMessage();
        sequenceDiagramMessage.setLabel(record.getHttpMethod() + " " + record.getUrl());
        sequenceDiagramMessage.setTo(record.getRecipient());
        sequenceDiagramMessage.setFrom(record.getSender());
        return sequenceDiagramMessage;
    }
}
