package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class FromTimeFilter extends Filter{
    private LocalDateTime from;

    public FromTimeFilter(LocalDateTime from) {
        this.from = from;
    }

    @Override
    boolean filter(Record record) {
        LocalDateTime time = record.getTime();
        return time.isEqual(from) || time.isAfter(from);
    }
}
