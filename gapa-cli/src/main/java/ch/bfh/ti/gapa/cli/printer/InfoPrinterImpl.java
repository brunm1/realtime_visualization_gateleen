package ch.bfh.ti.gapa.cli.printer;

import ch.bfh.ti.gapa.cli.exception.CommandLineException;
import ch.bfh.ti.gapa.cli.exception.CommandLineExceptionType;
import ch.bfh.ti.gapa.cli.exception.GapaCommandLineExceptionType;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Supplies methods to print out various data about
 * the application.
 */
public class InfoPrinterImpl implements InfoPrinter {
    private static final int WIDTH = 80;
    private static final int LEFT_PAD = 1;
    private static final int DESC_PAD = 3;
    private static final String APPLICATION_VERSION_PROPERTY = "application.version";
    static final String APP_PROPERTIES_RESOURCE_PATH = "/app.properties";

    private Options configOptions;
    private Options printOptions;
    PrintWriter pw;
    private String appDesc;
    private String command;
    private String appName;
    private CommandLineExceptionType[] commandLineExceptionTypes;
    private InputStream appPropertiesInputStream;

    InfoPrinterImpl(Options configOptions, Options printOptions, PrintWriter pw,
                    String appDesc, String command, String appName,
                    CommandLineExceptionType[] commandLineExceptionTypes,
                    InputStream appPropertiesInputStream) {
        this.configOptions = configOptions;
        this.printOptions = printOptions;
        this.pw = pw;
        this.appDesc = appDesc;
        this.command = command;
        this.appName = appName;
        this.commandLineExceptionTypes = commandLineExceptionTypes;
        this.appPropertiesInputStream = appPropertiesInputStream;
    }

    /**
     * Prints a description of the application, the command line options and the
     * possible exit codes.
     */
    @Override
    public void printHelp() {
        pw.println(appDesc);
        pw.println();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(pw, WIDTH,command,"", this.printOptions,
                LEFT_PAD, DESC_PAD,"", true);
        pw.println();
        pw.println("or");
        pw.println();
        helpFormatter.printHelp(pw, WIDTH,command,"", this.configOptions,
                LEFT_PAD,DESC_PAD,"", true);
        pw.println();
        pw.println(createCommandLineExceptionDoc());
    }

    private String createCommandLineExceptionDoc() {
        return "Possible exit codes:"+System.lineSeparator()+
                "0 - Normal termination."+System.lineSeparator()+
                Arrays.stream(commandLineExceptionTypes)
                .map(e->e.getCode()+" - "+e.getDesc()).collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Prints out the version of the application.
     * @throws IOException if version could not be read.
     */
    @Override
    public void printVersion() throws IOException {
        pw.println(appName+" - " + readVersion());
    }

    private String readVersion() throws IOException {
        Properties p = new Properties();
        p.load(appPropertiesInputStream);
        return p.getProperty(APPLICATION_VERSION_PROPERTY);
    }

    /**
     * Prints the description of the {@link GapaCommandLineExceptionType} and the
     * message of the cause.
     * @param e An exception generated by the CLI module that wraps the underlying exception.
     */
    @Override
    public void printCommandLineException(CommandLineException e) {
        pw.println(e.getCommandLineExceptionType().getDesc()+". Cause: " + e.getThrowable().getMessage());
    }
}
