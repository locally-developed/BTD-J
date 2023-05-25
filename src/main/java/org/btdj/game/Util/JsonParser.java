package org.btdj.game.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.btdj.game.Data.Bloons.Bloon;

public class JsonParser {
    public static Bloon getBloonFromJson(String name) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue("src/main/resources/assets/json/bloons.json", Bloon.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
