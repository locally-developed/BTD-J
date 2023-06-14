package org.btdj.game.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.btdj.game.Components.Towers.DartMonkeyComponent;
import org.btdj.game.Components.Towers.TackShooterComponent;
import org.btdj.game.EntityType;

public class TowerFactory implements EntityFactory {
    @Spawns("tower")
    public Entity newTower(SpawnData data) {
        Entity tower = FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.BLUE))
                .type(EntityType.TOWER)
                .with(new DartMonkeyComponent())
                .build();
        tower.addComponent(new DartMonkeyComponent());
        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));

        return tower;
    }

    @Spawns("tackShooter")
    public Entity newTackShooter(SpawnData data) {
        return FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.WHITE))
                .type(EntityType.TOWER)
                .with(new TackShooterComponent())
                .build();
    }
}
