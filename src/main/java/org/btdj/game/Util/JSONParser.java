package org.btdj.game.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.btdj.game.Data.Bloon;

public class JSONParser {
    public static Bloon getBloonFromJSON(String name) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue("src/main/resources/assets/json/bloons.json", Bloon.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
