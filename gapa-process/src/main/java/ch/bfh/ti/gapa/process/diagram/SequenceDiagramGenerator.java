package ch.bfh.ti.gapa.process.diagram;

import ch.bfh.ti.gapa.domain.recording.Record;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SequenceDiagramGenerator {

    public String generatePlantUmlSequenceDiagram(List<SequenceDiagramMessage> messages) {
        StringBuilder sb = new StringBuilder();

        sb
            .append("@startuml")
            .append("\n");

        //iterates over all messages and generates a line that depicts it
        for(SequenceDiagramMessage message : messages) {
            sb
                .append(message.getFrom())
                .append(" -> ")
                .append(message.getTo())
                .append(" : ")
                .append(message.getLabel())
                .append("\n");
        }

        sb
            .append("@enduml");

        return sb.toString();
    }

    public String generatePlantUmlSequenceDiagramFromRecords(List<Record> records) {
        List<SequenceDiagramMessage> sequenceDiagramMessages = new ArrayList<>();
        for(Record record : records) {
            sequenceDiagramMessages.add(SequenceDiagramMessage.createFromRecord(record));
        }
        return generatePlantUmlSequenceDiagram(sequenceDiagramMessages);
    }

    public void exportPlantUmlAsPng(String src, OutputStream target) throws IOException {
        SourceStringReader reader = new SourceStringReader(src);
        reader.outputImage(target, new FileFormatOption(FileFormat.PNG));
    }

    public void exportPlantUmlAsSvg(String src, OutputStream target) throws IOException {
        SourceStringReader reader = new SourceStringReader(src);
        reader.outputImage(target, new FileFormatOption(FileFormat.SVG));
    }
}