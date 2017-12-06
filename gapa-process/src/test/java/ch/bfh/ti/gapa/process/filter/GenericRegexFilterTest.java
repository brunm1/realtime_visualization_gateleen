package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class GenericRegexFilterTest {
    @Test
    void filter() {
        try {
            String filterString = ResourceReader.readStringFromResource("/GenericRegexFilterExample.json");
            Predicate<Record> filter = new GenericRegexFilter(new JSONObject(filterString));

            Record record = new Record();
            record.setHttpMethod("PUT");
            record.setRecipient("mars");
            record.setSender("jupiter");
            record.setTime(LocalDateTime.now());
            record.setUrl("/gateleen/server/events");

            assertTrue(filter.test(record));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}