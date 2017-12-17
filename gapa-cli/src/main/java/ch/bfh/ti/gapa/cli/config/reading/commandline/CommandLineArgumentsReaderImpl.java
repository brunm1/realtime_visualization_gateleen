package ch.bfh.ti.gapa.cli.config.reading.commandline;

import ch.bfh.ti.gapa.cli.config.model.ConfigField;
import ch.bfh.ti.gapa.cli.config.model.CliInput;
import org.apache.commons.cli.CommandLine;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads configuration data from the command line arguments.
 */
public class CommandLineArgumentsReaderImpl implements CommandLineArgumentsReader {
    private static final Logger LOGGER = Logger.getLogger(CommandLineArgumentsReaderImpl.class.getName());

    /**
     * Reads in remaining values from cli arguments.
     * Overwrites values from loaded config files.
     * Sets the default values if no value was set after reading cli arguments.
     * @param cliInput A {@link CliInput} instance. Configuration is read into this instance.
     * @param commandLine An instance of type {@link CommandLine} which contains command line options.
     */
    public void read(CliInput cliInput, CommandLine commandLine) {
        overwrite(cliInput::setWebsocketUri, cliInput::getWebsocketUri, "w", commandLine, ConfigField.websocketUri);
        overwrite(cliInput::setServerName, cliInput::getServerName, "n", commandLine, ConfigField.serverName);
    }

    private void overwrite(Consumer<String> setter, Supplier<String> getter, String option,
                           CommandLine commandLine, ConfigField configField){
        String oldValue = getter.get();
        if(commandLine.hasOption(option)) {
            String newValue = commandLine.getOptionValue(option);
            if(oldValue != null) {
                LOGGER.log(Level.CONFIG, "CLI argument overwrites config field " + configField.name() +". " +
                        "Old: "+ oldValue + " .New " + newValue);
            }
            setter.accept(newValue);
        } else {
            if(oldValue == null) {
                //apply default value
                setter.accept(configField.getDefaultValue());
            }
        }
    }
}
