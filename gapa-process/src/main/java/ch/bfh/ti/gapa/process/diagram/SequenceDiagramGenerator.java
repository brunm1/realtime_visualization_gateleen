package ch.bfh.ti.gapa.process.diagram;

import ch.bfh.ti.gapa.domain.recording.Record;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceDiagramGenerator {

    private Pattern simpleActorNamePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");

    private boolean doesActorNameNeedEscaping(String actorName) {
        Matcher matcher = simpleActorNamePattern.matcher(actorName);
        return !matcher.find();
    }

    private String escapeActorNameIfNeeded(String actorName) {
        if(doesActorNameNeedEscaping(actorName)) {
            return "\"" + actorName + "\"";
        } else {
            return actorName;
        }
    }

    public String generatePlantUmlSequenceDiagram(List<SequenceDiagramMessage> messages) {
        StringBuilder sb = new StringBuilder();

        sb
            .append("@startuml")
            .append(System.lineSeparator());

        //iterates over all messages and generates a line that depicts it
        for(SequenceDiagramMessage message : messages) {
            sb
                .append(escapeActorNameIfNeeded(message.getFrom()))
                .append(" -> ")
                .append(escapeActorNameIfNeeded(message.getTo()))
                .append(" : ")
                .append(message.getLabel())
                .append(System.lineSeparator());
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