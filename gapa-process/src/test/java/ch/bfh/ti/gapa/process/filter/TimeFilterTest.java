package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class TimeFilterTest {

    @Test
    public void filter() {
        try {
            String filterJson = ResourceReader.readStringFromResource("/TimeFilterExample.json");
            Predicate<Record> filter = new TimeFilter(new JSONObject(filterJson));

            Record record = new Record();
            record.setHttpMethod("PUT");
            record.setRecipient("mars");
            record.setSender("jupiter");
            record.setTime(LocalDateTime.now());
            record.setUrl("/gateleen/server/events");

            assertTrue(filter.test(record));

            filterJson = ResourceReader.readStringFromResource("/TimeFilterBeforeExample.json");
            filter = new TimeFilter(new JSONObject(filterJson));

            assertFalse(filter.test(record));

        } catch (IOException e) {
            //ToDo
        }
    }
}