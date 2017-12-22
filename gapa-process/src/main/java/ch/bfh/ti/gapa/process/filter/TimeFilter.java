package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

/**
 * A Filter that returns true if a record was sent before/after (depending on the configuration) the specified time.
 */
public class TimeFilter implements Predicate<Record> {
    private final static Schema schema;
    String name;
    LocalDateTime time;
    boolean before = false;

    static {
        String schemaString = "";
        try {
            schemaString = ResourceReader.readStringFromResource("/TimeFilter.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject o = new JSONObject(new JSONTokener(schemaString));
        schema = SchemaLoader.load(o);
    }

    /**
     * Creates a new TimeFilter from a JSONObject
     * @param filterJson JSONObject defining the filter.
     */
    TimeFilter(JSONObject filterJson) {
        try {
            schema.validate(filterJson);
            name = filterJson.optString("name", "");
            before = filterJson.optBoolean("before");
            time =  LocalDateTime.parse(filterJson.getString("time"), DateTimeFormatter.ISO_DATE_TIME);
        } catch (ValidationException e) {
            //ToDo
            e.printStackTrace();
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            //Todo: remove now() as default?
            time = LocalDateTime.now();
        }
    }

    /**
     * Applies the Filter to a given record
     * @param record Record to test
     * @return returns true if a record was sent before/after (depending on the configuration) the specified time
     */
    @Override
    public boolean test(Record record) {
        if(before) {
            return record.getTime().isBefore(time);
        } else {
            return record.getTime().isAfter(time);
        }
    }
}
