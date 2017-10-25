package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.util.regex.Pattern;

public class ReceiverFilter extends StringEqualFilter{
    public ReceiverFilter(String receiver) {
        super(receiver);
    }

    @Override
    String getValue(Record record) {
        return record.getRecipient();
    }
}
