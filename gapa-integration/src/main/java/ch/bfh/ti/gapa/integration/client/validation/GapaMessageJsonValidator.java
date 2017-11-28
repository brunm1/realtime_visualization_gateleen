package ch.bfh.ti.gapa.integration.client.validation;

import ch.bfh.ti.gapa.integration.client.socket.StringReceiver;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class GapaMessageJsonValidator implements StringReceiver{
    private Schema schema;
    private JsonReceiver jsonReceiver;

    public GapaMessageJsonValidator(JsonReceiver jsonReceiver) {
        //Get InputStream for json schema
        InputStream inputStream = this.getClass().getResourceAsStream("/gapa-message-schema.json");

        //Convert inputStream into org.json.JSONObject
        JSONObject o = new JSONObject(new JSONTokener(inputStream));

        //Convert JSONObject into schema
        schema = SchemaLoader.load(o);

        this.jsonReceiver = jsonReceiver;
    }

    @Override
    public void receiveString(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            try {
                schema.validate(jsonObject);
                jsonReceiver.receiveJson(jsonObject);
            } catch (ValidationException e) {
                System.out.println("Validation failed: " + e.toString());
            }
        } catch (JSONException e) {
            System.out.println("Could not parse json: " + e.toString());
        }
    }
}
