package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;

/**
 * Created by adrian on 18.10.17.
 */
@FunctionalInterface
public interface Filter {
    boolean filter(Record record);
}
