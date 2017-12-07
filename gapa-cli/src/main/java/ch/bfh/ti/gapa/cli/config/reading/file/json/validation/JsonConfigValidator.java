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
     */
    JSONObject validate(InputStream inputStream);
}
