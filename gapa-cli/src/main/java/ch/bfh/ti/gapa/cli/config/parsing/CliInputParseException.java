package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.model.ConfigField;
import ch.bfh.ti.gapa.cli.printer.InfoPrinterImpl;

/**
 * Depicts an exception thrown when parsing a field.
 * Wraps a {@link Throwable} and adds the {@link ConfigField}
 * that could not be parsed.
 */
public class CliInputParseException extends Throwable{
    private ConfigField configField;
    private Throwable reason;

    CliInputParseException(ConfigField configField, Throwable reason) {
        this.configField = configField;
        this.reason = reason;
    }

    /**
     * Used by {@link InfoPrinterImpl} to print the exception.
     * @return a Message describing the exception.
     */
    @Override
    public String getMessage() {
        return "Parsing failed on field " + configField + "." + System.lineSeparator() + reason.getMessage();
    }
}
