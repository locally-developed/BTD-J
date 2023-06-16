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

public class RoundHandler{
    private JsonNode roundData;
    private TimerAction spawnLoop = null;
    private final ArrayList<String > spawnBuffer = new ArrayList<>();
    private final ArrayList<ArrayList<BloonModifier>> spawnBufferModifiers = new ArrayList<>();
    private int processDelay = 0;

    public RoundHandler(int round) {
        try {
            this.roundData = JsonParser.getRoundData(round);
            loadBuffer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

         this.spawnLoop = FXGL.getGameTimer().runAtInterval(() -> {
             if (processDelay > 0 || MainApp.globalSpeedModifier > 1) {
                 if (spawnBuffer.isEmpty()) {
                     if (FXGL.getGameWorld().getEntitiesByType(EntityType.BLOON).isEmpty()) {
                         spawnLoop.expire();
                         MainApp.declareRoundComplete();
                     }
                     return;
                 }

                 spawn(spawnBuffer.get(0), spawnBufferModifiers.get(0));
                 spawnBuffer.remove(0);
                 spawnBufferModifiers.remove(0);
                 processDelay = 0;
             } else {
                 processDelay++;
             }
        }, Duration.seconds(0.5));
        spawnLoop.pause();
    }

    private void loadBuffer() {
        Iterator<Map.Entry<String, JsonNode>> nodes = this.roundData.fields();

        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();

            String type = entry.getKey();
            for (int i = 0; i < entry.getValue().size(); i++) {
                JsonNode spawnData = entry.getValue().get(i);
                int count = spawnData.get("count").asInt();

                for (int j = 0; j < count; j++) {
                    spawnBuffer.add(type);

                    ArrayList<BloonModifier> mods = new ArrayList<>();
                    for (int k = 0; k < spawnData.get("mods").size(); k++) {
                        mods.add(BloonModifier.valueOf(spawnData.get("mods").get(k).asText()));
                    }
                    spawnBufferModifiers.add(mods);
                }
            }
        }
    }

    public void start() {
        spawnLoop.resume();
    }

    public void spawn(String type, ArrayList<BloonModifier> modifiers) {
        Entity bloon = FXGL.getGameWorld().spawn("bloon");
        bloon.getComponent(BloonsComponent.class).updateProperties(type, modifiers);
        MainApp.bloonList.add(bloon);
    }
}
