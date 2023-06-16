package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import org.btdj.game.EntityType;

import java.util.List;


public class DartComponent extends Component {
    private double travelTime = 0;
    private Point2D velocity = new Point2D(0,0);

    @Override
    public void onUpdate(double tpf) {
        if (travelTime >= 0.25) FXGL.getGameWorld().removeEntity(entity);

        List<Entity> collidingBloons = FXGL.getGameWorld().getCollidingEntities(entity)
                .stream()
                .filter(e -> e.getType() == EntityType.BLOON)
                .toList();

        if (!collidingBloons.isEmpty()) {
            collidingBloons.get(0).getComponent(BloonsComponent.class).pop();
            FXGL.getGameWorld().removeEntity(entity);
        }

        entity.translate(velocity);
        travelTime+=tpf;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}
