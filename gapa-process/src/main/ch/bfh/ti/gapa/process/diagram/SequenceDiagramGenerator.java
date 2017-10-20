package ch.bfh.ti.gapa.process.diagram;

import java.util.List;

public class SequenceDiagramGenerator {

    public String generatePlantUmlSequenceDiagram(List<SequenceDiagramMessage> messages) {
        StringBuilder sb = new StringBuilder();

        //iterates over all messages and generates a line that depicts it
        for(SequenceDiagramMessage message : messages) {
            sb
                .append(message.getFrom())
                .append(" -> ")
                .append(message.getTo())
                .append(" : ")
                .append(message.getLabel());
        }

        return sb.toString();
    }
}