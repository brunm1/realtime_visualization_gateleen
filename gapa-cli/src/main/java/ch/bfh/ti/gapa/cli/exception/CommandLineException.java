package ch.bfh.ti.gapa.cli.exception;

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

    public Throwable getThrowable() {
        return throwable;
    }
}
