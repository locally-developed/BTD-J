package org.btdj.game.Logic;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.TimerAction;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.util.Duration;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.Components.BloonModifier;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;
import org.btdj.game.Util.JsonParser;

import java.io.IOException;
import java.util.*;

/**
 * Class containing all the bloon spawning logic, including the JSON parser and spawn buffers.
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class RoundHandler{
    private JsonNode roundData;
    private TimerAction spawnLoop = null;   //Declaring the spawnLoop within the class scope so every method can access
    private final ArrayList<String > spawnBuffer = new ArrayList<>();   //ArrayList containing bloons that need to spawn
    private final ArrayList<ArrayList<BloonModifier>> spawnBufferModifiers = new ArrayList<>();
    //ArrayList which is parallel to spawnBuffer, containing any modifiers that bloons will have
    private int processDelay = 0;   //Once the spawn loop is created, the interval at which it runs cannot be changed.
                                    //Therefore we use a counter to only run either every other cycle OR every cycle,
                                    //depending on the globalSpeedModifier.

    /**
     * Initializes a specified round, retrieves the round data, prepares the spawn buffer, and declares the spawn loop
     *
     * @param round round number
     */
    public RoundHandler(int round) {
        try {
            this.roundData = JsonParser.getRoundData(round);    //Getting the round data from the JSON file
            loadBuffer();
        } catch (IOException e) {
            System.out.println(e.getMessage()); //Debugging any errors without stopping; can't stop the train!
        }

         this.spawnLoop = FXGL.getGameTimer().runAtInterval(() -> { //Method which will be called every 0.5 seconds
             //If the speed modifier is x1, we'll run every other call to this lambda method.
             if (processDelay > 0 || MainApp.globalSpeedModifier > 1) {
                 if (spawnBuffer.isEmpty()) {   //Once we run out of bloons to spawn
                     //Check if any bloons still remain on the track
                     if (FXGL.getGameWorld().getEntitiesByType(EntityType.BLOON).isEmpty()) {
                         spawnLoop.expire();    //Close the spawn loop, leaving it for the JVM garbage collector
                         MainApp.declareRoundComplete();    //Invoking the post-round logic
                     }
                     return;
                 }

                 spawn(spawnBuffer.get(0), spawnBufferModifiers.get(0));    //Spawns the next bloon in the buffer
                 spawnBuffer.remove(0); //Removing the spawned bloon from the buffer
                 spawnBufferModifiers.remove(0);
                 processDelay = 0;
             } else {
                 processDelay++;
             }
        }, Duration.seconds(0.5));
        spawnLoop.pause();  //We'll let the start() method begin the spawn loop
    }

    /**
     * Parses the JsonNode containing the current round data, and adds the bloons to the spawn buffer.
     * Each round can only have 1 of each bloon type. Each bloon type may contain an unlimited number of variations,
     * including the ability to spawn both unmodified and modified bloons of the same type within the same round.
     */
    private void loadBuffer() {
        Iterator<Map.Entry<String, JsonNode>> nodes = this.roundData.fields();
        //Maps the JSON tree to a key-value pair, where the key is the bloon name and the value is the spawn info

        while (nodes.hasNext()) {   //Iterating over the different bloon types
            Map.Entry<String, JsonNode> entry = nodes.next();

            String type = entry.getKey();   //The bloon type
            for (int i = 0; i < entry.getValue().size(); i++) { //Iterating over the spawn info
                JsonNode spawnData = entry.getValue().get(i);   //Spawn parameters for the current variation
                int count = spawnData.get("count").asInt(); //How many bloons to be spawned of this type

                for (int j = 0; j < count; j++) {   //Each bloon will be its own element in the spawn buffer
                    spawnBuffer.add(type);  //Adding bloon to spawn buffer

                    ArrayList<BloonModifier> mods = new ArrayList<>();  //Creating a nested list for the modifiers
                    for (int k = 0; k < spawnData.get("mods").size(); k++) {
                        mods.add(BloonModifier.valueOf(spawnData.get("mods").get(k).asText()));
                        //Parsing the mods as BloonModifier enums, then adding them to the modifier list
                    }
                    spawnBufferModifiers.add(mods); //Adding the modifier list to the modifier buffer
                }
            }
        }
    }

    /**
     * Begins the round, starts spawning bloons
     */
    public void start() {
        spawnLoop.resume();
    }

    /**
     * Spawns a single bloon of given type and with given modifiers
     *
     * @param type bloon type
     * @param modifiers list of modifiers to apply to the bloon
     */
    public void spawn(String type, ArrayList<BloonModifier> modifiers) {
        Entity bloon = FXGL.getGameWorld().spawn("bloon");  //Creates a new bloon using the BloonFactory
        bloon.getComponent(BloonsComponent.class).updateProperties(type, modifiers); //Attaches modifiers to bloon
    }
}
