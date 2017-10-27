package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.util.regex.Pattern;

public abstract class PatternFilter implements Filter{
    private Pattern pattern;

    PatternFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    abstract String getValue(Record record);

    public boolean filter(Record record) {
        return pattern.matcher(this.getValue(record)).find();
    }
}
