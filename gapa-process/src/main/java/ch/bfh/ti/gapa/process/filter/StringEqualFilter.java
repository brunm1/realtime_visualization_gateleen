package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

public abstract class StringEqualFilter implements Filter{
    private String string;

    StringEqualFilter(String string) {
        this.string = string;
    }

    abstract String getValue(Record record);

    public boolean filter(Record record) {
        return string.equals(this.getValue(record));
    }
}
