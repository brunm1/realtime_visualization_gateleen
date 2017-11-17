package ch.bfh.ti.gapa.cli;

class CommandLineException extends Throwable {
    private Error error;
    private Throwable throwable;

    CommandLineException(Error error, Throwable throwable) {
        this.error = error;
        this.throwable = throwable;
    }

    Error getError() {
        return error;
    }

    Throwable getThrowable() {
        return throwable;
    }
}
