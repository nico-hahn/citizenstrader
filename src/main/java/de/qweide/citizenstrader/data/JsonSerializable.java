package de.qweide.citizenstrader.data;

import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface JsonSerializable {

    JSONObject toJson();

    Object fromJsonFile(String json) throws IOException;

    static JSONObject getJsonObjectFromFile(String fileName) throws IOException {
        return new JSONObject(
            Files.readString(Path.of(fileName))
        );
    }

    static JSONArray getJsonArrayFromFile(String fileName) throws IOException {
        return new JSONArray(
            Files.readString(Path.of(fileName))
        );
    }
}
