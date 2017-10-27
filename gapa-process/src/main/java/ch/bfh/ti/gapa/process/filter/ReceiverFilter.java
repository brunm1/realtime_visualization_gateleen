package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

class ReceiverFilter extends StringEqualFilter{
    ReceiverFilter(String receiver) {
        super(receiver);
    }

    @Override
    String getValue(Record record) {
        return record.getRecipient();
    }
}
