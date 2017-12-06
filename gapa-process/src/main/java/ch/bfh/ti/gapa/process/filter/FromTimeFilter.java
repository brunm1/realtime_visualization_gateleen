package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public class FromTimeFilter implements Predicate<Record> {
    private LocalDateTime from;

    FromTimeFilter(LocalDateTime from) {
        this.from = from;
    }

    @Override
    public boolean test(Record record) {
        LocalDateTime time = record.getTime();
        return time.isEqual(from) || time.isAfter(from);
    }
}
