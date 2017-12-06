package ch.bfh.ti.gapa.cli;

enum Error {
    FILE_NOT_FOUND(1, "Could not find file."),
    FAILED_PARSING_INBOUND_REQUEST_PATTERN(2, "Could not parse inbound request pattern."),
    FAILED_PARSING_OUTBOUND_REQUEST_PATTERN(3, "Could not parse outbound request pattern."),
    FAILED_PARSING_DATE_TIME_PATTERN(4, "Could not parse date time pattern."),
    INVALID_COMMAND_USAGE(5, "Invalid command usage."),
    UNRECOGNIZED_ARGUMENTS(6, "Could not recognize some arguments."),
    PROCESS_LOGIC_FAILED(7, "Error raised in process logic."),
    DEFAULT_CONFIG_PATH_NOT_VALID(8, "The syntax of the default config path is not valid"),
    WEBSOCKET_URI_NOT_VALID(9, "The syntax of the websocket uri is not valid."),
    DEFAULT_CONFIG_LOADING_FAILED(10, "Failed to load the default config."),
    USER_CONFIG_LOADING_FAILED(11, "Could not load user config"),
    CONFIG_PARSING_FAILED(12, "Could not parse configuration values");

    private int code;
    private String desc;

    Error(int code, String desc) {
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