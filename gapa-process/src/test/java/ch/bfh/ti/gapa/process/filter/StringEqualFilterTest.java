package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class StringEqualFilterTest {
    @Test
    void filter() {
        StringEqualFilter stringEqualFilter = new StringEqualFilter("a"){
            @Override
            String getValue(Record record) {
                return "a";
            }
        };
        assertTrue(stringEqualFilter.filter(null));
    }

}