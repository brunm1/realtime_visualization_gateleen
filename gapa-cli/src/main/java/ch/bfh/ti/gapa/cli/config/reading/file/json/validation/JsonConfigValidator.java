package ch.bfh.ti.gapa.cli.config.reading.file.json.validation;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Validates JSON config data.
 */
@FunctionalInterface
public interface JsonConfigValidator {

    /**
     * Validates JSON data from an {@link InputStream}
     * and returns a {@link JSONObject} if the data
     * was valid.
     * @param inputStream json data is read from this stream
     * @return a {@link JSONObject} if data was valid
     */
    JSONObject validate(InputStream inputStream);
}
