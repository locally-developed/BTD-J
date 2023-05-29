package org.btdj.game.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonParser {
    public static JsonNode getBloonFromJson(String name) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new File("src/main/resources/assets/json/bloons.json")).get(name);
    }
}
