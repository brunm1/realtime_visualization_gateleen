package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.domain.recording.RecordMock;
import ch.bfh.ti.gapa.domain.recording.Recording;
import ch.bfh.ti.gapa.domain.recording.RecordingMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class SubsetTest {
    @Test
    void getRecords() {
        Subset subset1 = new Subset(new ArrayList<>(), RecordingMock.recording1);
        assertArrayEquals(RecordingMock.recording1.getRecords().toArray(), subset1.getRecords().toArray());

//        Subset subset2 = new Subset(filters, RecordingMock.recording1);
//        assertArrayEquals(new Object[]{RecordMock.record1}, subset2.getRecords().toArray());
    }

}