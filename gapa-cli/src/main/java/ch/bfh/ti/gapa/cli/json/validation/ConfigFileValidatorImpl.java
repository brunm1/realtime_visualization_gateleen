package ch.bfh.ti.gapa.cli.json.validation;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class ConfigFileValidatorImpl implements ConfigFileValidator {
    private Schema schema;

    public ConfigFileValidatorImpl() {
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
            throw new IllegalArgumentException("Could not RawInputParserImpl config file content into json object.", e);
        }
    }

    public JSONObject validate(InputStream jsonInputStream) {
        JSONObject jsonObject = inputStreamToJsonObject(jsonInputStream);
        try {
            schema.validate(jsonObject);
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Validation of config file failed.", e);
        }
        return jsonObject;
    }
}
