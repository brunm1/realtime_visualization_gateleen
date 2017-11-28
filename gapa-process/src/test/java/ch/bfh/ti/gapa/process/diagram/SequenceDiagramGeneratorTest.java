package ch.bfh.ti.gapa.process.diagram;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SequenceDiagramGeneratorTest {
    @Test
    void generatePlantUmlSequenceDiagram() {
        List<SequenceDiagramMessage> messages = new ArrayList<>();
        SequenceDiagramMessage message = new SequenceDiagramMessage();
        message.setFrom("Spaceship");
        message.setTo("Houston");
        message.setLabel("We have a problem");
        messages.add(message);

        SequenceDiagramMessage weirdMessage = new SequenceDiagramMessage();
        weirdMessage.setFrom("/]{}à£è/&(/%");
        weirdMessage.setTo("What?");
        weirdMessage.setLabel("////>>>");
        messages.add(weirdMessage);

        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator();
        String result = sequenceDiagramGenerator.generatePlantUmlSequenceDiagram(messages);
        assertEquals("@startuml\n" +
                "Spaceship -> Houston : We have a problem\n" +
                "\"/]{}à£è/&(/%\" -> \"What?\" : ////>>>\n" +
                "@enduml", result);
    }

}