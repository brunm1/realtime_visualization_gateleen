package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AndFilterTest {
    @Test
    void filter() {
        String filterJson = null;
        try {
            filterJson = ResourceReader.readStringFromResource("/AndFilterExample.json");

            Filter filter = new AndFilter(new JSONObject(filterJson));

            Record record = new Record();
            record.setHttpMethod("PUT");
            record.setRecipient("mars");
            record.setSender("jupiter");
            record.setTime(LocalDateTime.now());
            record.setUrl("/gateleen/server/events");

            assertTrue(filter.filter(record));

            record.setUrl("/playground/test");
            assertFalse(filter.filter(record)); //The time filter should still be fine, but the regex filter won't return true
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}