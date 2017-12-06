package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.RecordMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FromTimeFilterTest {
    @Test
    void filter() {
        FromTimeFilter fromTimeFilter = new FromTimeFilter(LocalDateTime.parse("2017-10-25T12:00"));
        assertFalse(fromTimeFilter.test(RecordMock.record1));
        assertTrue(fromTimeFilter.test(RecordMock.record2));
    }

}