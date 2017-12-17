package ch.bfh.ti.gapa.cli.config.reading.file.json;

import ch.bfh.ti.gapa.cli.config.model.CliInput;
import org.json.JSONObject;

/**
 * Reads json into {@link CliInput}.
 */
@FunctionalInterface
public interface JsonReader {
    /**
     * Reads data from {@link JSONObject} into the intermediary type
     * {@link CliInput}.
     * @param cliInput target of copied data.
     * @param jsonObject data is read from this object
     */
    void read(CliInput cliInput, JSONObject jsonObject);
}