package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.RecordMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverFilterTest {
    @Test
    void getValue() {
        assertEquals("houston",new ReceiverFilter(null).getValue(RecordMock.record1));
    }

}