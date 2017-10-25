package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.RecordMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToTimeFilterTest {
    @Test
    void filter() {
        ToTimeFilter toTimeFilter = new ToTimeFilter(LocalDateTime.parse("2017-10-25T12:00"));
        assertTrue(toTimeFilter.filter(RecordMock.record1));
        assertFalse(toTimeFilter.filter(RecordMock.record2));
    }

}