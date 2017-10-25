package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.util.regex.Pattern;

public class SenderFilter extends StringEqualFilter{
    public SenderFilter(String sender) {
        super(sender);
    }

    @Override
    String getValue(Record record) {
        return record.getSender();
    }
}
