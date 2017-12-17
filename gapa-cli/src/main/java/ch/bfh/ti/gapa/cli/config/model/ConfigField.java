package ch.bfh.ti.gapa.cli.config.model;

/**
 * Contains all config field names
 * used for configuration.
 */
public enum ConfigField {
    websocketUri("ws://localhost:7012"),
    filters(null),
    serverName("gateway");

    private String defaultValue;

    ConfigField(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
