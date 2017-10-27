package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.util.regex.Pattern;

public class UrlFilter extends PatternFilter{

    public UrlFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    String getValue(Record record) {
        return record.getUrl();
    }
}
