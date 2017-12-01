package ch.bfh.ti.gapa.cli.parsing;

public class ParseException extends Throwable{
    private RawInputField rawInputField;
    private Throwable reason;

    public ParseException(RawInputField rawInputField, Throwable reason) {
        this.rawInputField = rawInputField;
        this.reason = reason;
    }
}
