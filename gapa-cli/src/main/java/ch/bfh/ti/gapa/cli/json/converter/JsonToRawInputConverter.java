package ch.bfh.ti.gapa.cli.json.converter;

import ch.bfh.ti.gapa.cli.raw.RawInput;
import org.json.JSONObject;

public interface JsonToRawInputConverter {
    RawInput convert(JSONObject jsonObject);
}