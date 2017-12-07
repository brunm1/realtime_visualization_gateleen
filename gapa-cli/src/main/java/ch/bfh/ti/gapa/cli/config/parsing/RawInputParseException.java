package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInputField;

/**
 * Depicts an exception thrown when parsing a field.
 * Wraps a {@link Throwable} and adds the {@link RawInputField}
 * that could not be parsed.
 */
public class RawInputParseException extends Throwable{
    private RawInputField rawInputField;
    private Throwable reason;

    RawInputParseException(RawInputField rawInputField, Throwable reason) {
        this.rawInputField = rawInputField;
        this.reason = reason;
    }

    /**
     * Used by {@link ch.bfh.ti.gapa.cli.printer.InfoPrinter} to print the exception.
     * @return a Message describing the exception.
     */
    @Override
    public String getMessage() {
        return "Parsing failed on field " + rawInputField + ".\n" + reason.getMessage();
    }
}
