package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrFilterTest {
    @Test
    void filter() {
        try {
            String filterJson = ResourceReader.readStringFromResource("/OrFilterExample.json");

            Filter filter = new OrFilter(new JSONObject(filterJson));

            Record record = new Record();
            record.setHttpMethod("PUT");
            record.setRecipient("mars");
            record.setSender("jupiter");
            record.setTime(LocalDateTime.now());
            record.setUrl("/gateleen/server/events");

            assertTrue(filter.filter(record));

            record.setUrl("/playground/test");
            assertTrue(filter.filter(record)); //The time filter should still be fine, but the regex filter won't return true

            record.setTime(LocalDateTime.MIN);
            assertFalse(filter.filter(record));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}