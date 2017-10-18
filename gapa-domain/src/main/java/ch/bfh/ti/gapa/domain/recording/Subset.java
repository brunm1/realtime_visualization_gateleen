package ch.bfh.ti.gapa.domain.recording;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 18.10.17.
 */
public class Subset {
    private Recording recording;
    private List<Filter> filters;
    private String name;

    public Subset() {
        filters = new ArrayList<>();
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
