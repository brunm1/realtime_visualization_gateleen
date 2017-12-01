package ch.bfh.ti.gapa.cli.json.validation;

import org.json.JSONObject;

import java.io.InputStream;

public interface ConfigFileValidator {
    JSONObject validate(InputStream inputStream);
}
