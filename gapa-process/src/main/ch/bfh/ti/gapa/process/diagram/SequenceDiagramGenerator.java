package ch.bfh.ti.gapa.process.diagram;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.IOException;
import java.io.OutputStream;
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

    public void exportPlantUmlAsPng(String src, OutputStream target) throws IOException {
        SourceStringReader reader = new SourceStringReader(src);
        reader.outputImage(target, new FileFormatOption(FileFormat.PNG));
    }

    public void exportPlantUmlAsSvg(String src, OutputStream target) throws IOException {
        SourceStringReader reader = new SourceStringReader(src);
        reader.outputImage(target, new FileFormatOption(FileFormat.SVG));
    }
}