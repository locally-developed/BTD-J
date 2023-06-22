package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import org.btdj.game.EntityType;

import java.util.List;

/**
 * Handles the behavior of dart projectiles
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class DartComponent extends Component {
    private double travelTime = 0;  //Total time-of-flight
    private Point2D velocity = new Point2D(0,0);

    @Override
    public void onUpdate(double tpf) {
        if (travelTime >= 0.25) FXGL.getGameWorld().removeEntity(entity);
        //Removing the dart once it's travelled for 250ms

        List<Entity> collidingBloons = FXGL.getGameWorld().getCollidingEntities(entity)
                .stream()
                .filter(e -> e.getType() == EntityType.BLOON)
                .toList();
        //Gets entities the dart collides with and filters out any entity which is NOT a bloon

        if (!collidingBloons.isEmpty()) {   //Check if we actually collided with something
            collidingBloons.get(0).getComponent(BloonsComponent.class).pop();   //Popping the colliding bloon
            FXGL.getGameWorld().removeEntity(entity);   //Removing the now-used dart
        }

        entity.translate(velocity); //Translating by our velocity every frame
        travelTime+=tpf;    //Incrementing our time-of-flight
    }

    /**
     * @param velocity velocity vector of dart
     */
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}
