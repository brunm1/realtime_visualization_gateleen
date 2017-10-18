package ch.bfh.ti.gapa.domain.recording;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 18.10.17.
 */
public class Recording {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Record> records;

    public Recording() {
        records = new ArrayList<>();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
