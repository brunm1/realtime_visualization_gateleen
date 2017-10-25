package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.util.regex.Pattern;

public abstract class StringEqualFilter extends Filter{
    private String string;

    public StringEqualFilter(String string) {
        this.string = string;
    }

    abstract String getValue(Record record);

    public boolean filter(Record record) {
        return string.equals(this.getValue(record));
    }
}
