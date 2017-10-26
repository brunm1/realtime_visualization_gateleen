package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;

public class FromTimeFilter implements Filter{
    private LocalDateTime from;

    FromTimeFilter(LocalDateTime from) {
        this.from = from;
    }

    @Override
    public boolean filter(Record record) {
        LocalDateTime time = record.getTime();
        return time.isEqual(from) || time.isAfter(from);
    }
}
