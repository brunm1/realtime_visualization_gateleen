package ch.bfh.ti.gapa.test;

import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the application like DefaultApplicationTest does, but uses a custom config file which contains filters to test the filtering logic.
 */
public class FilterApplicationTest extends ApplicationTest {
    @Override
    void test() throws IOException, InterruptedException {
        List<String> mockServerArgs = new ArrayList<>();
        mockServerArgs.add("-c");
        mockServerArgs.add("./src/test/resources/filterConfig.json");
        connectGapaWithMockServer(mockServerArgs);

        //ToDo: Send more messages
        List<GapaMessage> messages = new ArrayList<>();
        GapaMessage m1 = new GapaMessage();
        m1.setMethod(GapaMessage.Method.GET);
        m1.setPeer("mars");
        m1.setPath("/gateleen/server/test/foo/bar");
        m1.setTimestamp(Instant.now());
        m1.setType(GapaMessage.Type.inbound);
        messages.add(m1);

        GapaMessage m2 = new GapaMessage();
        m2.setMethod(GapaMessage.Method.GET);
        m2.setPeer("mars");
        m2.setPath("/playground/server/test/foo/bar");
        m2.setTimestamp(Instant.now());
        m2.setType(GapaMessage.Type.inbound);
        messages.add(m2);

        serverMock.sendGapaMessages(messages);

        //get output
        ProcessOutput out = getProcessOutput();

        //check output:

        //expect exit code to be 0
        Assertions.assertEquals(0, out.getExitCode());

        //expect output to be the expected plantuml
        String expectedPlantUml = ResourceReader.readStringFromResource("/expectedFiltered.plantuml");

        String[] logLines = out.getStdErr().split("\n");

        if(runJar) {

            //Check stderr output
            Assertions.assertEquals(2, logLines.length);

            //expect opened connection
//            Assertions.assertTrue(logLines[1].contains("Opened connection"));
        } else {
//            Assertions.assertEquals(2, logLines.length);
            expectedPlantUml = expectedPlantUml.split("\n", 2)[1] ;
//            Assertions.assertTrue(logLines[1].contains("Opened connection"));
        }

        Assertions.assertEquals(expectedPlantUml, out.getStdOut());



        //It is possible that the plantuml output cannot be rendered.
        //PlantUml has no usable API to check plantUml Syntax, so we check if the
        //generated svg is the same as a valid svg we checked ourselves.
        String expectedSvg = ResourceReader.readStringFromResource("/expected.svg");

        //We collect the bytes of the output in an in-memory byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new SequenceDiagramGenerator().exportPlantUmlAsSvg(expectedPlantUml,byteArrayOutputStream);
        //convert bytes to string
        String actualSvg = byteArrayOutputStream.toString("utf8");

        //Use following code to copy svg to resources.
        //Check if svg can be rendered in browser!
//        FileOutputStream outputStream = new FileOutputStream(new File(this.getClass().getResource("/expected.svg").getFile()));
//        outputStream.write(actualSvg.getBytes());

//        Assertions.assertEquals(expectedSvg, actualSvg); //ToDo
    }
}
