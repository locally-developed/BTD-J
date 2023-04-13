package org.btdj.game.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.MainApp;

public class BloonFactory implements EntityFactory {
    @Spawns("bloon")
    public Entity newBloon(SpawnData data) {
        Entity bloon = FXGL.entityBuilder(data)
                .view("bloons/blue.png")
                .type(MainApp.EntityType.BLOON)
                .build();
        bloon.addComponent(new BloonsComponent());

        return bloon;
    }
}
