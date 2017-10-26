package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

class HttpMethodFilter extends StringEqualFilter{
    HttpMethodFilter(String s) {
        super(s);
    }

    @Override
    String getValue(Record record) {
        return record.getHttpMethod();
    }
}
