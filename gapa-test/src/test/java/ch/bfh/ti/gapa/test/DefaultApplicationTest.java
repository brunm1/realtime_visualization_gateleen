package ch.bfh.ti.gapa.test;

import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

class DefaultApplicationTest extends ApplicationTest{

    /**
     * Runs the generated gapa.jar with the parameter -w. It is expected that most users
     * will use the application this way. The server is mocked in this test. The server sends dummy messages to
     * gapa. The recording stops and exit code, stdout and stderr are verified. It is also verified
     * if the resulting PlantUml can be rendered as SVG.
     */
    @Override
    void test() throws Throwable {
        connectGapaWithMockServer(new ArrayList<>());

        sendSampleMessagesToGapa();

        //get output
        ProcessOutput out = getProcessOutput();

        //check output:

        //expect exit code to be 0
        Assertions.assertEquals(0, out.getExitCode());

        String actualPlantUml = out.getStdOut();
        //expect output to be the expected plantuml
        String expectedPlantUml = ResourceReader.readStringFromResource("/expected.plantuml");
        Assertions.assertEquals(expectedPlantUml, actualPlantUml, "If not the same, generate new plantuml for test resources and render svg and check if it is correct.");

        if(runJar) {
            //Check stderr output
            String[] logLines = out.getStdErr().split(System.lineSeparator());
            Assertions.assertEquals(1, logLines.length);

            //expect opened connection
            Assertions.assertTrue(logLines[0].contains("Opened connection"));
        }
        //TODO implement a way to specifically verify log output of cli module when not run as a jar.

        //It is possible that the plantuml output cannot be rendered.
        //PlantUml has no usable API to check plantUml Syntax, so we check if the
        //generated svg doesn't contain a specific error string

        //We collect the bytes of the output in an in-memory byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new SequenceDiagramGenerator().exportPlantUmlAsSvg(actualPlantUml,byteArrayOutputStream);
        //convert bytes to string
        String actualSvg = byteArrayOutputStream.toString("utf8");

        Assertions.assertFalse(actualSvg.contains(">Syntax Error?</text>"), "Svg should not contain a syntax error message.");
    }
}
