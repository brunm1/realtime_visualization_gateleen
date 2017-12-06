package ch.bfh.ti.gapa.cli.config.reading.file.json.validation;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Validates json config data.
 */
public interface JsonConfigValidator {

    /**
     * Validates JSON data from an InputStream
     * and returns a JSONObject if the data
     * was valid.
     */
    JSONObject validate(InputStream inputStream);
}
