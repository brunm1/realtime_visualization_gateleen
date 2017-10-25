package ch.bfh.ti.gapa.domain.recording;

import java.time.LocalDateTime;

public class RecordingMock {
    public static Recording getRecording1() {
        Recording recording = new Recording();
        recording.getRecords().add(RecordMock.getRecord1());
        recording.getRecords().add(RecordMock.getRecord2());
        return recording;
    }
}