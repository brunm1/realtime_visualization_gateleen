package ch.bfh.ti.gapa.cli.printer;

import ch.bfh.ti.gapa.cli.config.CliConfigOptions;
import ch.bfh.ti.gapa.cli.exception.GapaCommandLineExceptionType;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Configures gapa specific parameters for the {@link InfoPrinterImpl}
 * and adds the method {@link #printConfigSchema()} to print the
 * json schema used to validate config data.
 */
public class GapaInfoPrinterImpl extends InfoPrinterImpl implements GapaInfoPrinter {
    private static final String CONFIG_SCHEMA_JSON_RESOURCE_PATH = "/config-schema.json";
    private static final int JSON_SCHEMA_INDENT_FACTOR = 2;
    private static final String APP_DESC = "Visualizes communication between services." +System.lineSeparator()+
            "It connects to a communication gateway over websocket,"+System.lineSeparator()+
            "receives communication data, filters it and outputs PlantUml."+System.lineSeparator()+
            "Filters can be configured with a json config file.";
    private static final String COMMAND = "java -jar /path/to/gapa.jar";
    private static final String APP_NAME = "Gapa";

    public GapaInfoPrinterImpl(PrintWriter pw) {
        super(new CliConfigOptions(), new CliPrintOptions(), pw, APP_DESC, COMMAND, APP_NAME,
                GapaCommandLineExceptionType.values(),
                GapaInfoPrinterImpl.class.getResourceAsStream(APP_PROPERTIES_RESOURCE_PATH));
    }

    /**
     * Loads the json schema and prints it.
     * @throws IOException if schema cannot be loaded
     */
    @Override
    public void printConfigSchema() throws IOException {
        try (InputStream inputStream = GapaInfoPrinterImpl.class.getResourceAsStream(CONFIG_SCHEMA_JSON_RESOURCE_PATH)) {
            pw.println(new JSONObject(new JSONTokener(inputStream)).toString(JSON_SCHEMA_INDENT_FACTOR));
        }
    }
}
