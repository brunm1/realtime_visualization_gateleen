package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterConverter {
    private static Schema schema;

    static {
        InputStream inputStream = FilterConverter.class.getResourceAsStream("/FilterArray.json");
        JSONObject o = new JSONObject(new JSONTokener(inputStream));
        schema = SchemaLoader.load(o);
    }

    public static List<Predicate<Record>> convert(JSONArray jsonArray) {
        schema.validate(jsonArray);

        ArrayList<Predicate<Record>> filters = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++) {
            filters.add(convertToFilter(jsonArray.getJSONObject(i)));
        }
        return filters;
    }

    public static Predicate<Record>  convertToFilter(JSONObject filterJson) {
        switch (filterJson.getString("type")) {
            case "genericRegexFilter":
                return new GenericRegexFilter(filterJson);
            case "timeFilter":
                return new TimeFilter(filterJson);
            case "andFilter":
                return new AndFilter(filterJson);
            case "orFilter":
                return new OrFilter(filterJson);
            default:
                throw new IllegalArgumentException("Unsupported filter type \"" + filterJson.getString("type") + "\"");
        }
    }
}
