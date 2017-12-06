package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.resources.ResourceReader;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericRegexFilter implements Predicate<Record> {
    private final static Schema schema;
    private String name = "";
    private Pattern pattern;
    private String attribute;

    static {
        String schemaString = "";
        try {
            schemaString = ResourceReader.readStringFromResource("/GenericRegexFilter.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject o = new JSONObject(new JSONTokener(schemaString));
        schema = SchemaLoader.load(o);
    }

    public GenericRegexFilter(JSONObject filterJson) {
        try {
            schema.validate(filterJson);
            if(filterJson.has("name")) {
                name = filterJson.getString("name");
            }
            pattern = Pattern.compile(filterJson.getString("pattern"));
            attribute = filterJson.getString("attribute");
        } catch (ValidationException e) {
            //ToDo
        }
    }

    @Override
    public boolean test(Record record) {
        if(record.get(attribute) == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(record.get(attribute));
        return matcher.matches();
    }
}
