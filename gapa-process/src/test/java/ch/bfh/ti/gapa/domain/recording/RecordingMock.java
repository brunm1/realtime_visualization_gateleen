package ch.bfh.ti.gapa.domain.recording;

public class RecordingMock {
    public static Recording recording1 = new Recording();
    static {
        recording1.getRecords().add(RecordMock.record1);
        recording1.getRecords().add(RecordMock.record2);
    }
    public static Recording copy(Recording recording) {
        Recording copy = new Recording();
        copy.getRecords().add(RecordMock.copy(RecordMock.record2));
        copy.getRecords().add(RecordMock.copy(RecordMock.record2));
        return copy;
    }
}