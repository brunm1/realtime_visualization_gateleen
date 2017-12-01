package ch.bfh.ti.gapa.cli.loader;

import ch.bfh.ti.gapa.cli.json.converter.JsonToRawInputConverter;
import ch.bfh.ti.gapa.cli.json.validation.ConfigFileValidator;
import ch.bfh.ti.gapa.cli.parsing.ParseException;
import ch.bfh.ti.gapa.cli.parsing.RawInputParser;
import ch.bfh.ti.gapa.cli.raw.RawInput;
import ch.bfh.ti.gapa.process.interfaces.Input;
import org.apache.commons.cli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;


public class CommandLineArgumentsLoaderImpl implements CommandLineArgumentsLoader{

    private ConfigFileValidator configFileValidator;
    private JsonToRawInputConverter jsonToRawInputConverter;
    private RawInputParser rawInputParser;


    public CommandLineArgumentsLoaderImpl(ConfigFileValidator configFileValidator, JsonToRawInputConverter jsonToRawInputConverter, RawInputParser rawInputParser) {
        this.configFileValidator = configFileValidator;
        this.jsonToRawInputConverter = jsonToRawInputConverter;
        this.rawInputParser = rawInputParser;
    }

    public void loadInput(Input input, CommandLine commandLine) throws ParseException {
        if(commandLine.hasOption("c")) {
            Path userConfigPath = Paths.get(commandLine.getOptionValue("c"));

            ConfigLoader configLoader = new UserConfigLoader(
                    configFileValidator,
                    jsonToRawInputConverter,
                    rawInputParser,
                    userConfigPath
            );
            configLoader.loadInput(input, false);
        }

        if(commandLine.hasOption("w")) {
            RawInput rawInput = new RawInput();
            rawInput.setWebsocketUri(commandLine.getOptionValue("w"));
            rawInputParser.parse(rawInput, input);
        }
    }
}
