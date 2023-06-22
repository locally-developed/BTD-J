package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.EntityType;

import java.util.List;

/**
 * Handles the bloon waypoint functionality
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class WaypointComponent extends Component {
    private final Point2D direction;

    /**
     * @param direction unit vector direction which bloons shall travel
     */
    public WaypointComponent(Point2D direction) {
        this.direction = direction;
    }
    @Override
    public void onUpdate(double tpf) {  //time per frame
        List<Entity> entitiesNearby = FXGL.getGameWorld().getEntitiesInRange(  //Gets any entities touching the waypoint
                new Rectangle2D(entity.getX()-5, entity.getY()-5, 10, 10)
        );
        entitiesNearby = entitiesNearby.stream().filter(e -> e.getType() == EntityType.BLOON).toList();
        //Filters out any entities that are not bloons

        for (Entity bloon: entitiesNearby) {
            //Ignoring bloons already headed in the waypoint's direction
            if (bloon.getComponent(BloonsComponent.class).getVelocity() == this.direction) continue;
            bloon.getComponent(BloonsComponent.class).setVelocity(direction);   //Changing the bloon direction of travel
        }
    }
}
