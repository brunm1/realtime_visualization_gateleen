package ch.bfh.ti.gapa.domain.recording;

import java.time.LocalDateTime;

public class RecordMock {
    public static Record getRecord1() {
        Record record = new Record();
        record.setHttpMethod("GET");
        record.setRecipient("houston");
        record.setSender("spaceship");
        record.setTime(LocalDateTime.parse("2017-10-25T00:00"));
        return record;
    }
    public static Record getRecord2() {
        Record record = new Record();
        record.setHttpMethod("PUT");
        record.setRecipient("earth");
        record.setSender("mars");
        record.setTime(LocalDateTime.parse("2017-10-26T00:00"));
        return record;
    }
}