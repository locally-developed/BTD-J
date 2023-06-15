package org.btdj.game.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;

public class BloonFactory implements EntityFactory {
    @Spawns("bloon")
    public Entity newBloon(SpawnData data) {
        return FXGL.entityBuilder(data)
                .at(MainApp.BLOON_SPAWN)
                .view("bloons/red/default.png")
                .bbox(BoundingShape.box(50,50))
                .type(EntityType.BLOON)
                .with(new BloonsComponent())
                .build();
    }
}
