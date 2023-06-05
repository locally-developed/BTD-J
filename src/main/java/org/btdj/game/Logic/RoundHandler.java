package org.btdj.game.Logic;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.TimerAction;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.util.Duration;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.MainApp;
import org.btdj.game.Util.JsonParser;

import java.io.IOException;

public class RoundHandler {
    public RoundHandler(int round) {

    }

    public void start() {
        JsonNode levelData;
        try {
            levelData = JsonParser.getRoundData(1);

            for (int i = 0; i < levelData.get("red").asInt(); i++) {
                FXGL.getGameTimer().runOnceAfter(() -> {
                    spawn("green");
                }, Duration.seconds(0.5));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void spawn(String type) {
        Entity bloon = FXGL.getGameWorld().spawn("bloon");
        bloon.getComponent(BloonsComponent.class).updateProperties(type);
        MainApp.bloonList.add(bloon);
    }
}
