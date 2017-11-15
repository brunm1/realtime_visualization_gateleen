package ch.bfh.ti.gapa.playground;

import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.resources.ResourceReader;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads a plantUml diagram from sample.plantuml, renders it as png
 * and opens it in firefox.
 * Can be used to try out plantuml.
 */
public class PlantUmlPreview {
    public static void main(String[] args) throws IOException {
        String plantUml = ResourceReader.readStringFromResource("/sample.plantuml");
        SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator();
        Path path = Files.createTempFile("gapa-playground", ".svg");
        OutputStream os = Files.newOutputStream(path);
        sequenceDiagramGenerator.exportPlantUmlAsSvg(plantUml,os);
        Runtime.getRuntime().exec("firefox " + path.toAbsolutePath().toString());
    }
}