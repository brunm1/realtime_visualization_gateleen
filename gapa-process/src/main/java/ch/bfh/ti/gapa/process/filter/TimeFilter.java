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

public class TimeFilter implements Filter{
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

    @Override
    public boolean filter(Record record) {
        if(before) {
            return record.getTime().isBefore(time);
        } else {
            return record.getTime().isAfter(time);
        }
    }
}
