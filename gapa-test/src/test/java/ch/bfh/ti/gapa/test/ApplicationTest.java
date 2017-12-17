package ch.bfh.ti.gapa.test;

import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Is used as the base class for application tests.
 * An application test runs the packaged command line application (gapa.jar)
 * with the "java -jar" command and
 * verifies its output.
 */
abstract class ApplicationTest {
    private ServerMock serverMock;
    private Process process;

    /**
     * Find executable gapa.jar. It is a jar that contains all needed dependencies.
     * @return File of the jar
     */
    private File findGapaJar(){
        File dir = new File("../gapa-cli/target");
        File[] files = dir.listFiles((dir1, filename) -> filename.equals("gapa.jar"));
        File cliJar;
        if(files == null || files.length != 1) {
            throw new AssertionError("Expected gapa.jar in target folder of gapa-cli module but it's not there.");
        } else {
            cliJar = files[0];
        }
        return cliJar;
    }

    /**
     * We run the jar with the help of Java's ProcessBuilder.
     */
    private void runGapaJar(List<String> moreArgs) throws IOException {
        File cliJar = findGapaJar();
        //concat process builder arguments to run the jar
        List<String> args = new ArrayList<>();
        args.add("java");
        args.add("-jar");
        args.add(cliJar.getAbsolutePath());
        args.addAll(moreArgs);

        ProcessBuilder builder = new ProcessBuilder(args);
        process = builder.start();
    }

    /**
     * Starts a server mock on a random port and runs gapa.jar with
     * the correct arguments to connect to it.
     * @param moreArgs Additional arguments for gapa
     * @throws IOException can be raised when trying to run gapa.jar
     */
    void connectGapaWithMockServer(List<String> moreArgs) throws IOException {
        //setup mock server which can sends messages to a connected gapa instance
        serverMock = new ServerMock();

        List<String> args = new ArrayList<>();
        args.add("-w");
        args.add("ws://localhost:"+serverMock.getPort());
        args.addAll(moreArgs);

        runGapaJar(args);

        //Wait until client has connected
        serverMock.waitForConnection();
    }

    /**
     * Sends the Enter key stroke to the gapa process.
     * @throws IOException can be raised on sending the key stroke.
     */
    private void simulateUserInput() throws IOException {
        //The recording has to be terminated by the user by typing in arbitrary input
        //E.g. 10 for enter key
        process.getOutputStream().write(10);
        process.getOutputStream().close();
    }

    /**
     * Aggregates the whole gapa process output.
     */
    class ProcessOutput {
        String stdOut;
        String stdErr;
        int exitCode;

        public String getStdOut() {
            return stdOut;
        }

        void setStdOut(String stdOut) {
            this.stdOut = stdOut;
        }

        public String getStdErr() {
            return stdErr;
        }

        void setStdErr(String stdErr) {
            this.stdErr = stdErr;
        }

        public int getExitCode() {
            return exitCode;
        }

        void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }
    }

    /**
     * Reads all bytes from the stream.
     * @param inputStream StdOut or StdErr
     * @return the bytes interpreted as UTF-8 text
     * @throws IOException when reading fails
     */
    private String getProcessOutput(InputStream inputStream) throws IOException {
        return StringFromInputStreamReader.readStringFromInputStream(
                inputStream, Charset.forName("UTF-8"), 1024
        );
    }

    /**
     * Stops gapa recording by sending an Enter key stroke to gapa.
     * Collects the gapa output and returns it.
     * @return aggregated process output
     * @throws IOException when reading of stdout or stderr fails
     * @throws InterruptedException when gapa process was interrupted
     */
    ProcessOutput getProcessOutput() throws IOException, InterruptedException {
        //terminates recording
        simulateUserInput();

        ProcessOutput processOutput = new ProcessOutput();
        processOutput.setExitCode(process.waitFor());
        processOutput.setStdErr(getProcessOutput(process.getErrorStream()));
        processOutput.setStdOut(getProcessOutput(process.getInputStream()));

        return processOutput;
    }

    /**
     * Sends some dummy messages from the server mock to a connected gapa websocket client.
     */
    void sendSampleMessagesToGapa() {
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
        serverMock.sendGapaMessages(messages);
    }
}
