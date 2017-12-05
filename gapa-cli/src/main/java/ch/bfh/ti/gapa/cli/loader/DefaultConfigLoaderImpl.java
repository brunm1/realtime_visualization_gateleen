package ch.bfh.ti.gapa.cli.loader;

import ch.bfh.ti.gapa.cli.Cli;
import ch.bfh.ti.gapa.cli.json.converter.JsonToRawInputConverter;
import ch.bfh.ti.gapa.cli.json.validation.ConfigFileValidator;
import ch.bfh.ti.gapa.cli.parsing.ParseException;
import ch.bfh.ti.gapa.cli.parsing.RawInputParser;
import ch.bfh.ti.gapa.process.interfaces.Input;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultConfigLoaderImpl extends ConfigLoader implements DefaultConfigLoader {

    public DefaultConfigLoaderImpl(ConfigFileValidator configFileValidator,
                                   JsonToRawInputConverter jsonToRawInputConverter, RawInputParser rawInputParser) {
        super(configFileValidator, jsonToRawInputConverter, rawInputParser);
    }

    public void loadInput(Input input) throws ParseException {
        super.loadInput(input, true);
    }

    @Override
    Path createConfigFilePath() throws URISyntaxException {
        return getPathToExecutedJar().getParent().resolve("config.json");
    }

    private static Path getPathToExecutedJar() throws URISyntaxException {
        return Paths.get(Cli.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }
}
