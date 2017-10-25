package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.util.regex.Pattern;

public class HttpMethodFilter extends StringEqualFilter{
    public HttpMethodFilter(String s) {
        super(s);
    }

    @Override
    String getValue(Record record) {
        return record.getHttpMethod();
    }
}
