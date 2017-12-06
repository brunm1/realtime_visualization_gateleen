package ch.bfh.ti.gapa.cli.config.reading.file.json;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import org.json.JSONObject;

/**
 * Reads json to RawInput.
 */
@FunctionalInterface
public interface JsonReader {
    /**
     * Reads data from JSONObject into the intermediary type
     * RawInput.
     */
    void read(RawInput input, JSONObject jsonObject);
}