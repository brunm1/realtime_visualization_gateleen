package ch.bfh.ti.gapa.cli.json.validation;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Validates json config data.
 */
public interface ConfigFileValidator {

    /**
     * Validates JSON data from an InputStream
     * and returns a JSONObject if the data
     * was valid.
     */
    JSONObject validate(InputStream inputStream);
}
