package org.btdj.game.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;

/**
 * Factory responsible for spawning bloons into the game
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class BloonFactory implements EntityFactory {
    @Spawns("bloon")    //Interface so we can call this method using a string, rather than reference
    public Entity newBloon(SpawnData data) {    //Method which is called when attempting to spawn a bloon
        return FXGL.entityBuilder(data)
                .at(MainApp.BLOON_SPAWN)    //Global bloon spawn point
                .bbox(BoundingShape.box(100,130))   //Collision box, necessary since the bloon is an image
                .type(EntityType.BLOON)
                .with(new BloonsComponent())    //Attaches the bloon component
                .build();   //Game engine will finish making our entity, eliminates boring code
    }
}
