import ch.bfh.ti.gapa.process.reader.StreamRedirection;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class End2EndTest {
    @Test
    public void end2endTest() throws IOException, InterruptedException {
        //Find executable
        File dir = new File("../gapa-cli/target");
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.startsWith("gapa-cli-");
            }
        });
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
                "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+) - %(\\S+)\\s+(?<method>GET|PUT|POST|DELETE)\\s+(?<url>\\S+)\\s+s=(?<sender>\\w+)",
                "yyyy-MM-dd HH:mm:ss,SSS");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        //set text from sample log as StdIn
        InputStream stdIn = ResourceReader.class.getResourceAsStream("/sample_log");
        StreamRedirection.redirectStream(stdIn, process.getOutputStream(), 1024);
        //get result from StdOut
        String stdOut = StringFromInputStreamReader.readStringFromInputStream(process.getInputStream(), Charset.forName("UTF-8"), 1024);

        int exitValue = process.waitFor();
        String expectedPlantUml = ResourceReader.readStringFromResource("/expected.plantuml");
        Assertions.assertEquals(0, exitValue);
        Assertions.assertEquals(expectedPlantUml, stdOut);
    }
}
