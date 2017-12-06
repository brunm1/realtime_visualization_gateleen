package ch.bfh.ti.gapa.cli;

class CommandLineException extends Throwable {
    private ExceptionType exceptionType;
    private Throwable throwable;

    CommandLineException(ExceptionType exceptionType, Throwable throwable) {
        this.exceptionType = exceptionType;
        this.throwable = throwable;
    }

    ExceptionType getExceptionType() {
        return exceptionType;
    }

    Throwable getThrowable() {
        return throwable;
    }
}
