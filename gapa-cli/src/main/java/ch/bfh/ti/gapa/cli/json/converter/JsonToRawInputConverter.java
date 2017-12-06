package ch.bfh.ti.gapa.cli.json.converter;

import ch.bfh.ti.gapa.cli.raw.RawInput;
import org.json.JSONObject;

/**
 * Converts json to RawInput.
 */
@FunctionalInterface
public interface JsonToRawInputConverter {
    /**
     * Converts a JSONObject into the intermediary type
     * RawInput.
     * @param jsonObject an already validated json object
     * @return a RawInput instance that contains the data from the json object
     */
    RawInput convert(JSONObject jsonObject);
}