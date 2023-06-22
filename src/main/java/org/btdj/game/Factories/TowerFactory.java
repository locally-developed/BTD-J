package org.btdj.game.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.btdj.game.Components.Towers.*;
import org.btdj.game.EntityType;

/**
 * Factory responsible for spawning towers into the game
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class TowerFactory implements EntityFactory {
    //Literally the same code as in every other factory class
    @Spawns("dartMonkey")
    public Entity newDartMonkey(SpawnData data) {
        Entity tower = FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.BLUE))
                .type(EntityType.TOWER)
                .with(new DartMonkeyComponent())
                .build();
        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));    //Allows us to rotate the tower about the centre

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
    @Spawns("bombShooter")
    public Entity newBombShooter(SpawnData data) {
        Entity tower = FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.RED))
                .type(EntityType.TOWER)
                .with(new BombShooterComponent())
                .build();
        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));

        return tower;
    }
    @Spawns("glueGunner")
    public Entity newGlueGunner(SpawnData data) {
        Entity tower = FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.PURPLE))
                .type(EntityType.TOWER)
                .with(new GlueGunnerComponent())
                .build();
        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));

        return tower;
    }
    @Spawns("dartlingGunner")
    public Entity newDartlingGunner(SpawnData data) {
        Entity tower = FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.ORANGE))
                .type(EntityType.TOWER)
                .with(new DartlingGunnerComponent())
                .build();
        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));

        return tower;
    }
    @Spawns("iceMonkey")
    public Entity newIceMonkey(SpawnData data) {
        Entity tower = FXGL.entityBuilder()
                .view(new Rectangle(25, 25, Color.YELLOW))
                .type(EntityType.TOWER)
                .with(new IceMonkeyComponent())
                .build();
        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));

        return tower;
    }
}
