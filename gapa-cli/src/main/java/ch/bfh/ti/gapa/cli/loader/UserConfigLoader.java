package ch.bfh.ti.gapa.cli.loader;

import ch.bfh.ti.gapa.cli.json.converter.JsonToRawInputConverter;
import ch.bfh.ti.gapa.cli.json.validation.ConfigFileValidator;
import ch.bfh.ti.gapa.cli.parsing.RawInputParser;

import java.nio.file.Path;

public class UserConfigLoader extends ConfigLoader{

    private Path userConfigPath;

    UserConfigLoader(ConfigFileValidator configFileValidator,
                     JsonToRawInputConverter jsonToRawInputConverter,
                     RawInputParser rawInputParser,
                     Path userConfigPath) {
        super(configFileValidator, jsonToRawInputConverter, rawInputParser);

        this.userConfigPath = userConfigPath;
    }

    @Override
    Path createConfigFilePath() {
        return this.userConfigPath;
    }
}
