package ch.bfh.ti.gapa.cli.exception;

/**
 * If an exception occurs while running the CLI application,
 * the exception is wrapped and a {@link CommandLineExceptionType} is added.
 */
public class CommandLineException extends Throwable {
    private CommandLineExceptionType commandLineExceptionType;
    private Throwable throwable;

    public CommandLineException(CommandLineExceptionType commandLineExceptionType, Throwable throwable) {
        this.commandLineExceptionType = commandLineExceptionType;
        this.throwable = throwable;
    }

    public CommandLineExceptionType getCommandLineExceptionType() {
        return commandLineExceptionType;
    }

    /**
     * @return the wrapped exception
     */
    public Throwable getThrowable() {
        return throwable;
    }
}
