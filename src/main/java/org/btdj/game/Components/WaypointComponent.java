package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;

import java.util.List;

public class WaypointComponent extends Component {
    private final Point2D direction;
    public WaypointComponent(Point2D direction) {
        this.direction = direction;
    }
    @Override
    public void onUpdate(double tpf) {
        List<Entity> entitiesNearby = FXGL.getGameWorld().getEntitiesInRange(
                new Rectangle2D(entity.getX()-25, entity.getY()-25, 50, 50)
        );
        entitiesNearby = entitiesNearby.stream().filter(e -> e.getType() == EntityType.BLOON).toList();

        for (Entity bloon: entitiesNearby) {
            if (bloon.getComponent(BloonsComponent.class).getVelocity() == this.direction) continue;
            bloon.getComponent(BloonsComponent.class).setVelocity(direction);
        }
    }
}
