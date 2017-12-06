package ch.bfh.ti.gapa.process.filter;

import ch.bfh.ti.gapa.domain.recording.Record;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterConverter {
    public static List<Predicate<Record>> convert(JSONArray jsonArray) {
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
            default:
                //ToDo
        }

        return null;
    }
}
