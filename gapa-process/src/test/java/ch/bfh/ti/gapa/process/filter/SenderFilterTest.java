package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.RecordMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenderFilterTest {
    @Test
    void getValue() {
        assertEquals("spaceship",new  SenderFilter(null).getValue(RecordMock.record1));

    }

}