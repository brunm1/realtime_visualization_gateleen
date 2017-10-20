package ch.bfh.ti.gapa.playground;

import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlantUmlPreview {
    public static void main(String[] args) throws IOException {
        String plantUml = "Spaceship -> Houston : We have a problem";
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator();
        Path path = Files.createTempFile("gapa-playground", ".png");
        OutputStream os = Files.newOutputStream(path);
        sequenceDiagramGenerator.exportPlantUmlAsPng(plantUml,os);
        Runtime.getRuntime().exec("firefox " + path.toAbsolutePath().toString());
    }
}