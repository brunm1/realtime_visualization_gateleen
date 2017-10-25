package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;

public class ToTimeFilter extends Filter{
    private LocalDateTime to;

    public ToTimeFilter(LocalDateTime to) {
        this.to = to;
    }

    @Override
    boolean filter(Record record) {
        LocalDateTime time = record.getTime();
        return time.isEqual(to) || time.isBefore(to);
    }
}
