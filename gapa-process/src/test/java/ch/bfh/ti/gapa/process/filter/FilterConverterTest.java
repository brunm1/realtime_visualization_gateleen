package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import org.everit.json.schema.ValidationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class FilterConverterTest {
    @Test
    public void convert() {
        JSONArray jsonArray = readJsonArray("/FiltersExample.json");
        List<Predicate<Record>> list = FilterConverter.convert(jsonArray);
        assertEquals(4, list.size());

        assertThrows(ValidationException.class, () -> {
             FilterConverter.convert(readJsonArray("/FiltersExampleBad.json"));});
    }
    @Test
    public void convertToFilter() {
        assertThrows(IllegalArgumentException.class, () -> {
            FilterConverter.convertToFilter(new JSONObject(new JSONTokener(this.getClass().getResourceAsStream("/FilterUnknownType.json"))));});
    }

    private JSONArray readJsonArray(String name) {
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        return new JSONArray((new JSONTokener(inputStream)));
    }
}