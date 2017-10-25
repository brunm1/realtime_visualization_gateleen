package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.domain.recording.Recording;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Subset {
    private List<Filter> filters;
    private Recording recording;

    public Subset(List<Filter> filters, Recording recording) {
        this.filters = filters;
        this.recording = recording;
    }

    /**
     * Applies the filters on the records of the recording
     * @return a list of records
     */
    List<Record> getRecords() {
        Stream<Record> stream = recording.getRecords().stream();
        for(Filter filter: filters) {
            stream = stream.filter(filter::filter);
        }
        return stream.collect(Collectors.toList());
    }
}
