package ch.bfh.ti.gapa.domain.recording;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecordMock {
    public static Record record1 = new Record();
    static {
        record1.setHttpMethod("GET");
        record1.setRecipient("houston");
        record1.setSender("spaceship");
        record1.setTime(LocalDateTime.parse("2017-10-25T00:00", DateTimeFormatter.ISO_DATE_TIME));
    }
    public static Record record2 = new Record();
    static {
        record2.setHttpMethod("PUT");
        record2.setRecipient("earth");
        record2.setSender("mars");
        record2.setTime(LocalDateTime.parse("2017-10-26T00:00", DateTimeFormatter.ISO_DATE_TIME));
    }
    public static Record copy(Record record) {
        Record copy = new Record();
        copy.setTime(record.getTime());
        copy.setSender(record.getSender());
        copy.setRecipient(record.getRecipient());
        copy.setHttpMethod(record.getHttpMethod());
        return copy;
    }
}