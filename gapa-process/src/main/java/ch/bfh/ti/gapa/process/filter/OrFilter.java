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
 * Filter that consists of a List of filters. It returns true if at least one of the filters returns true.
 */
public class OrFilter implements Predicate<Record> {
    List<Predicate<Record>> filters;
    String name;
    private static final Schema schema;

    static {
        String schemaString = "";
        try {
            schemaString = ResourceReader.readStringFromResource("/OrFilter.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject o = new JSONObject(new JSONTokener(schemaString));
        schema = SchemaLoader.load(o);
    }

    /**
     * Creates a new OrFilter from a JSONObject
     * @param filterJson JSONObject defining the filter.
     */
    public OrFilter(JSONObject filterJson) {
        schema.validate(filterJson);
        name = filterJson.optString("name", "");
        filters = FilterConverter.convert(filterJson.getJSONArray("filters"));
    }

    /**
     * Applies the Filter to a given record
     * @param record Record to test
     * @return true if the record passes at least one filter in the list of filters
     */
    @Override
    public boolean test(Record record) {
        Iterator<Predicate<Record>> it = filters.iterator();
        while (it.hasNext()) {
            if(it.next().test(record)) {
                return true;
            }
        }

        return false;
    }
}
