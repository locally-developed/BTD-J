package org.btdj.game.Logic;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.TimerAction;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.util.Duration;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.MainApp;
import org.btdj.game.Util.JsonParser;

import java.io.IOException;
import java.util.*;

public class RoundHandler {
    private JsonNode roundData;
    private TimerAction spawnLoop = null;
    private final ArrayList<String> spawnBuffer = new ArrayList<>();

    public RoundHandler(int round) {
        try {
            this.roundData = JsonParser.getRoundData(round);
            loadBuffer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

         this.spawnLoop = FXGL.getGameTimer().runAtInterval(() -> {
            if (spawnBuffer.isEmpty()) {
                spawnLoop.pause();
                return;
            }

            spawn(spawnBuffer.get(0));
            spawnBuffer.remove(0);
        }, Duration.seconds(1));
        spawnLoop.pause();
    }

    private void loadBuffer() {
        Iterator<Map.Entry<String, JsonNode>> nodes = this.roundData.fields();

        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();

            String type = entry.getKey();
            int count = entry.getValue().asInt();

            for (int i = 0; i < count; i++) {
                spawnBuffer.add(type);
            }
        }
    }

    public void start() {
        spawnLoop.resume();
    }

    public void spawn(String type) {
        Entity bloon = FXGL.getGameWorld().spawn("bloon");
        bloon.getComponent(BloonsComponent.class).updateProperties(type);
        MainApp.bloonList.add(bloon);
    }
}
