package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

class SenderFilter extends StringEqualFilter{
    SenderFilter(String sender) {
        super(sender);
    }

    @Override
    String getValue(Record record) {
        return record.getSender();
    }
}
