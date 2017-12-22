package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Filter that consists of a List of filters. It returns true if all filters return true.
 */
public class AndFilter implements Predicate<Record> {
    List<Predicate<Record>> filters;
    String name;
    private static final Schema schema;

    static {
        String schemaString = "";
        try {
            schemaString = ResourceReader.readStringFromResource("/AndFilter.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject o = new JSONObject(new JSONTokener(schemaString));
        schema = SchemaLoader.load(o);
    }

    /**
     * Creates a new AndFilter from a JSONObject
     * @param filterJson JSONObject defining the filter.
     */
    public AndFilter(JSONObject filterJson) {
        schema.validate(filterJson);
        name = filterJson.optString("name", "");
        filters = FilterConverter.convert(filterJson.getJSONArray("filters"));
    }

    /**
     * Applies the Filter to a given record
     * @param record Record to test
     * @return true if the record passes all filters in the list of filters
     */
    @Override
    public boolean test(Record record) {
        Iterator<Predicate<Record>> it = filters.iterator();
        while (it.hasNext()) {
            if(!it.next().test(record)) {
                return false;
            }
        }

        return true;
    }
}
