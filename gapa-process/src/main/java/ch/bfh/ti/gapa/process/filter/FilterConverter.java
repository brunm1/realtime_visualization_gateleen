package ch.bfh.ti.gapa.process.filter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterConverter {
    public static List<Filter> convert(JSONArray jsonArray) {
        ArrayList<Filter> filters = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++) {
            filters.add(convertToFilter(jsonArray.getJSONObject(i)));
        }
        return filters;
    }

    public static Filter convertToFilter(JSONObject filterJson) {
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
