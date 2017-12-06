package ch.bfh.ti.gapa.cli.config.parsing;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInputField;

public class ParseException extends Throwable{
    private RawInputField rawInputField;
    private Throwable reason;

    public ParseException(RawInputField rawInputField, Throwable reason) {
        this.rawInputField = rawInputField;
        this.reason = reason;
    }
}
