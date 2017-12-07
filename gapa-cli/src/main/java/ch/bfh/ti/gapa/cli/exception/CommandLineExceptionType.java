package ch.bfh.ti.gapa.cli.exception;

public enum CommandLineExceptionType {
    INVALID_COMMAND_USAGE(5, "Invalid command usage."),
    UNRECOGNIZED_ARGUMENTS(6, "Could not recognize some arguments."),
    PROCESS_LOGIC_FAILED(7, "Exception raised in process logic."),
    DEFAULT_CONFIG_READING_FAILED(10, "Failed to load the default config."),
    CONFIG_PARSING_FAILED(12, "Could not parse configuration values"),
    COMMAND_LINE_CONFIG_READING_FAILED(13, "Could not read configuration from command line arguments");

    private int code;
    private String desc;

    CommandLineExceptionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}