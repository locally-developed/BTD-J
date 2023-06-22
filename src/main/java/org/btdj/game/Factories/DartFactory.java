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

/**
 * Factory responsible for spawning darts into the game
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class DartFactory implements EntityFactory {
    @Spawns("dart")    //Interface so we can call this method using a string, rather than reference
    public Entity newDart(SpawnData data) {    //Method which is called when attempting to spawn a dart
        return FXGL.entityBuilder()
                .view(new Rectangle(2,10, Color.BLACK)) //Making the dart a small, slender rectangle
                .type(EntityType.PROJECTILE)
                .bbox(BoundingShape.box(10,10)) //Collision box so that we can tell when we hit a bloon
                .with(new DartComponent())    //Attaches the dart component
                .build();
    }
}
