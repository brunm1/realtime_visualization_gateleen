package ch.bfh.ti.gapa.cli;

enum ExceptionType {
    INVALID_COMMAND_USAGE(5, "Invalid command usage."),
    UNRECOGNIZED_ARGUMENTS(6, "Could not recognize some arguments."),
    PROCESS_LOGIC_FAILED(7, "Exception raised in process logic."),
    DEFAULT_CONFIG_LOADING_FAILED(10, "Failed to load the default config."),
    USER_CONFIG_LOADING_FAILED(11, "Could not load user config"),
    CONFIG_PARSING_FAILED(12, "Could not parse configuration values");

    private int code;
    private String desc;

    ExceptionType(int code, String desc) {
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