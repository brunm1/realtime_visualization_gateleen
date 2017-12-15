package ch.bfh.ti.gapa.test;

import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.reader.StreamRedirection;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class End2EndTest {
    static ServerMock serverMock;
    
    List<GapaMessage> createSampleMessages() {
        List<GapaMessage> messages = new ArrayList<>();
        
        GapaMessage m1 = new GapaMessage();
        m1.setMethod(GapaMessage.Method.GET);
        m1.setPeer("mars");
        m1.setType(GapaMessage.Type.inbound);
        m1.setPath("/earth/services/moon/status");
        m1.setTimestamp(Instant.now());
        messages.add(m1);
        
        GapaMessage m2 = new GapaMessage();
        m2.setMethod(GapaMessage.Method.PUT);
        m2.setPeer("uranus");
        m2.setType(GapaMessage.Type.inbound);
        m2.setPath("/earth/time/v1/offset/_hooks/listeners/http/stuff-044829350048546734");
        m2.setTimestamp(Instant.now());
        messages.add(m2);

        GapaMessage m3 = new GapaMessage();
        m3.setMethod(GapaMessage.Method.PUT);
        m3.setPeer("uranus");
        m3.setType(GapaMessage.Type.inbound);
        m3.setPath("/earth/user/registration/v1/signon/_hooks/listeners/http/stuff-03810166830522095");
        m3.setTimestamp(Instant.now());
        messages.add(m3);

        return messages;
    }

    @BeforeAll
    static void beforeAll() {
        //setup mock server which receives requests and sends data about them to a connected gapa instance
        serverMock = new ServerMock();
    }

    @Test
    void defaultStart() throws IOException, InterruptedException {
        //Find executable gapa.jar. It is a jar that contains all needed dependencies.
        File dir = new File("../gapa-cli/target");
        File[] files = dir.listFiles((dir1, filename) -> filename.equals("gapa.jar"));
        File cliJar;
        if(files == null || files.length != 1) {
            throw new AssertionError("Expected gapa.jar in target folder of gapa-cli module but it's not there.");
        } else {
            cliJar = files[0];
        }

        //We run the jar as close as possible to a real execution in the command line
        //The server was started on a random port. The port is passed as a command line argument.
        ProcessBuilder builder = new ProcessBuilder(
                "java", "-jar",
                cliJar.getAbsolutePath(),
                "-w", "ws://localhost:"+serverMock.getPort());
        Process process = builder.start();

        //Wait until client has connected
        serverMock.waitForConnection();

        //Send mock data from server to gapa
        serverMock.sendGapaMessages(createSampleMessages());

        //Wait until gapa messages are received by client
        //Testing in an asynchronous environment is difficult.
        //40ms is an approximation. Gapa has no capabilities to tell
        //how many messages were already received. We can't ouput
        //this information in StdOut because this channel is reserved for
        //PlantUml output.
        Thread.sleep(40);

        //The recording has to be terminated by the user by typing in arbitrary input
        //E.g. 10 for enter key
        process.getOutputStream().write(10);
        process.getOutputStream().close();

        //get result from StdOut
        String stdOut = StringFromInputStreamReader.readStringFromInputStream(
                process.getInputStream(), Charset.forName("UTF-8"), 1024
        );

        //get result from StdErr
        String stdErr = StringFromInputStreamReader.readStringFromInputStream(
                process.getErrorStream(), Charset.forName("UTF-8"), 1024
        );

        //waits until process is done and returns exit code of the process
        int exitValue = process.waitFor();

        //expect exit code to be 0
        Assertions.assertEquals(0, exitValue);

        //expect output to be the expected plantuml
        String expectedPlantUml = ResourceReader.readStringFromResource("/expected.plantuml");
        Assertions.assertEquals(expectedPlantUml, stdOut);

        String[] logLines = stdErr.split("\n");
        Assertions.assertEquals(4, logLines.length);

        //expect info output about missing config file (it is optional)
        Assertions.assertTrue(logLines[1].contains("INFO: Config file not found:"));
        //expect opened connection
        Assertions.assertTrue(logLines[3].contains("Opened connection"));

        //It is possible that the plantuml output cannot be rendered.
        //PlantUml has no usable API to check plantUml Syntax, so we check if the
        //generated svg is the same as a valid svg we checked ourselves.
        String expectedSvg = ResourceReader.readStringFromResource("/expected.svg");

        //We collect the bytes of the output in an in-memory byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new SequenceDiagramGenerator().exportPlantUmlAsSvg(expectedPlantUml,byteArrayOutputStream);
        //convert bytes to string
        String actualSvg = byteArrayOutputStream.toString("utf8");

        Assertions.assertEquals(expectedSvg, actualSvg);
    }
}
