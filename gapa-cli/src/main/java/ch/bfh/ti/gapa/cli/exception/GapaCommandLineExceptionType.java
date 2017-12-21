package ch.bfh.ti.gapa.cli.exception;

/**
 * Defines exit codes with descriptions.
 * If an exception happens in the cli application, the
 * exception is wrapped and one of the following {@link GapaCommandLineExceptionType} is added.
 */
public enum GapaCommandLineExceptionType implements CommandLineExceptionType {
    INVALID_COMMAND_USAGE(5, "Invalid command usage."),
    UNRECOGNIZED_ARGUMENTS(6, "Could not recognize some arguments."),
    PROCESS_LOGIC_FAILED(7, "Exception raised in process logic."),
    CONFIG_PARSING_FAILED(12, "Could not parse configuration values."),
    COMMAND_LINE_CONFIG_READING_FAILED(13, "Could not read configuration from command line arguments."),
    PRINT_VERSION_FAILED(14, "Could not print version."),
    PRINT_CONFIG_SCHEMA_FAILED(15, "Could not print config schema."),
    CONFIG_FILE_READING_FAILED(16, "Failed to load config file");

    private int code;
    private String desc;

    GapaCommandLineExceptionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}