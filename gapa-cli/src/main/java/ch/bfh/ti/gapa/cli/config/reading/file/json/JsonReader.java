package ch.bfh.ti.gapa.cli.config.reading.file.json;

import ch.bfh.ti.gapa.cli.config.reading.model.RawInput;
import org.json.JSONObject;

/**
 * Reads json into {@link RawInput}.
 */
@FunctionalInterface
public interface JsonReader {
    /**
     * Reads data from {@link JSONObject} into the intermediary type
     * {@link RawInput}.
     * @param rawInput target of copied data.
     * @param jsonObject data is read from this object
     */
    void read(RawInput rawInput, JSONObject jsonObject);
}