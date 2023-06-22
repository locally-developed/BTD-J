package org.btdj.game.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonParser {
    /**
     * Retrieves info regarding a specific bloon from bloons.json
     *
     * @param name name of bloon
     * @return JsonNode object containing bloon data
     * @throws IOException error parsing JSON data
     */
    public static JsonNode getBloonFromJson(String name) throws IOException {
        ObjectMapper mapper = new ObjectMapper();   //Creates a new Jackson ObjectMapper, which maps JSON to Objects
        return mapper.readTree(new File("src/main/resources/assets/json/bloons.json")).get(name);
        //Loads, reads, and retrieves the specific branch of JSON
    }

    /**
     * Retrieves info regarding which bloons to spawn during a specific round
     *
     * @param round round number
     * @return JsonNode object containing round data
     * @throws IOException error parsing JSON data
     */
    public static JsonNode getRoundData(int round) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new File("src/main/resources/assets/json/rounds.json")).get(Integer.toString(round));
    }
}
