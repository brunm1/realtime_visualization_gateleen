package ch.bfh.ti.gapa.cli;

enum Error {
    FILE_NOT_FOUND(1, "Could not find file."),
    FAILED_PARSING_INBOUND_REQUEST_PATTERN(2, "Could not parse inbound request pattern."),
    FAILED_PARSING_OUTBOUND_REQUEST_PATTERN(3, "Could not parse outbound request pattern."),
    FAILED_PARSING_DATE_TIME_PATTERN(4, "Could not parse date time pattern."),
    INVALID_COMMAND_USAGE(5, "Invalid command usage."),
    UNRECOGNIZED_ARGUMENTS(6, "Could not recognize some arguments.");

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