package ch.bfh.ti.gapa.cli.config.reading.file.json.validation;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

/**
 * Validates configuration JSON data. It uses
 * the config-schema.json JSON schema for that.
 */
public class JsonConfigValidatorImpl implements JsonConfigValidator {
    private Schema schema;

    public JsonConfigValidatorImpl() {
        //Get InputStream for json schema
        InputStream schemaInputStream = this.getClass().getResourceAsStream("/config-schema.json");

        //Convert inputStream into org.json.JSONObject
        JSONObject o = new JSONObject(new JSONTokener(schemaInputStream));

        //Convert JSONObject into schema
        schema = SchemaLoader.load(o);
    }

    private JSONObject inputStreamToJsonObject(InputStream jsonInputStream) {
        try {
            return new JSONObject(new JSONTokener(jsonInputStream));
        } catch (JSONException e) {
            throw new IllegalArgumentException("Could not convert config into json object.", e);
        }
    }

    /**
     * Uses the schema to validate the data and returns a
     * {@link JSONObject} if the validation was successful.
     * @param jsonInputStream input stream containing json data
     * @return a valid json object if json data was valid
     * @throws IllegalArgumentException if the data was not valid
     */
    @Override
    public JSONObject validate(InputStream jsonInputStream) {
        JSONObject jsonObject = inputStreamToJsonObject(jsonInputStream);
        try {
            schema.validate(jsonObject);
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Validation of config json failed.", e);
        }
        return jsonObject;
    }
}
