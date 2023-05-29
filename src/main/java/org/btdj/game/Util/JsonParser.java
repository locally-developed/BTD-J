package org.btdj.game.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.btdj.game.Data.Bloons.Bloon;

import java.io.File;

public class JsonParser {
    public static JsonNode getBloonFromJson(String name) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(new File("src/main/resources/assets/json/bloons.json"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
