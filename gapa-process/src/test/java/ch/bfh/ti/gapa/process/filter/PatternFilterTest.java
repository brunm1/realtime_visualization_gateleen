package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PatternFilterTest {
    @Test
    void filter() {
        PatternFilter patternFilter = new PatternFilter(Pattern.compile("a")){
            @Override
            String getValue(Record record) {
                return "a";
            }
        };
        assertTrue(patternFilter.filter(null));
    }

}