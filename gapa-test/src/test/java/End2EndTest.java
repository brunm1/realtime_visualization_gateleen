import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.reader.StreamRedirection;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;

class End2EndTest {
    @Test
    @Disabled //TODO run websocket server instead of parsing logs
    void end2endTest() throws IOException, InterruptedException {
        //Find executable
        File dir = new File("../gapa-cli/target");
        File[] files = dir.listFiles((dir1, filename) -> filename.startsWith("gapa-cli-"));
        File cliJar;
        if(files == null || files.length == 0) {
            throw new AssertionError("expected gapa-cli jar in target folder of gapa-cli module");
        } else {
            cliJar = files[0];
        }

        //config process
        ProcessBuilder builder = new ProcessBuilder(
                "java", "-jar",
                cliJar.getAbsolutePath(),
                "-w", "ws://localhost:8888");
        builder.redirectErrorStream(true);
        Process process = builder.start();

        //get result from StdOut
        String stdOut = StringFromInputStreamReader.readStringFromInputStream(process.getInputStream(), Charset.forName("UTF-8"), 1024);

        int exitValue = process.waitFor();
        String expectedPlantUml = ResourceReader.readStringFromResource("/expected.plantuml");
        Assertions.assertEquals(0, exitValue);
        Assertions.assertEquals(expectedPlantUml, stdOut);

        //test if the plant uml can be rendered
        String expectedSvg = ResourceReader.readStringFromResource("/expected.svg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new SequenceDiagramGenerator().exportPlantUmlAsSvg(expectedPlantUml,byteArrayOutputStream);
        String actualSvg = byteArrayOutputStream.toString("utf8");
        Assertions.assertEquals(expectedSvg, actualSvg);
    }
}
