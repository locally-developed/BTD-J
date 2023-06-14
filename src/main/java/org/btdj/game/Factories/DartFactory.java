package org.btdj.game.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.btdj.game.Components.DartComponent;
import org.btdj.game.EntityType;

public class DartFactory implements EntityFactory {
    @Spawns("dart")
    public Entity newDart(SpawnData data) {
        return FXGL.entityBuilder()
                .view(new Rectangle(10,10, Color.RED))
                .type(EntityType.PROJECTILE)
                .bbox(BoundingShape.box(10,10))
                .with(new DartComponent())
                .build();
    }
}
