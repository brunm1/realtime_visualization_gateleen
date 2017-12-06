package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class OrFilterTest {
    @Test
    void filter() {
        try {
            String filterJson = ResourceReader.readStringFromResource("/OrFilterExample.json");

            Predicate<Record> filter = new OrFilter(new JSONObject(filterJson));

            Record record = new Record();
            record.setHttpMethod("PUT");
            record.setRecipient("mars");
            record.setSender("jupiter");
            record.setTime(LocalDateTime.now());
            record.setUrl("/gateleen/server/events");

            assertTrue(filter.test(record));

            record.setUrl("/playground/test");
            assertTrue(filter.test(record)); //The time filter should still be fine, but the regex filter won't return true

            record.setTime(LocalDateTime.MIN);
            assertFalse(filter.test(record));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}