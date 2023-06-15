package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.Components.Towers.Interfaces.TrackTower;
import org.btdj.game.EntityType;

import java.util.Comparator;
import java.util.List;

public class TowerComponent extends Component {
    private final GameWorld world = FXGL.getGameWorld();
    protected TargetingPriority targetingPriority = TargetingPriority.FIRST;
    protected Rectangle2D rangeCollider;
    public void setRangeCollider(Rectangle2D collider) {
        this.rangeCollider = collider;
    }

    public void trackTower(TrackTower trackTower) {
        List<Entity> bloonsInRange = world.getEntitiesInRange(this.rangeCollider);

        switch (targetingPriority) {
            case FIRST:
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == EntityType.BLOON)
                        .toList();
                break;
            case STRONGEST:
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == EntityType.BLOON)
                        .sorted(Comparator.comparingInt(bloon ->
                                bloon.getComponent(BloonsComponent.class).getRBE())
                        )
                        .toList();
                break;
        }

        if (!bloonsInRange.isEmpty()) {
            Entity target = null;
            switch (targetingPriority) {
                case FIRST, WEAKEST -> target = bloonsInRange.get(0);
                case LAST, STRONGEST -> target = bloonsInRange.get(bloonsInRange.size() - 1);
            }

            Point2D p1 = target.getPosition().subtract(entity.getPosition());
            Point2D lookVector = new Point2D(1, 0);
            double angle = lookVector.angle(p1);

            entity.setRotation(angle);

            trackTower.invoke(target);
        }
    }
}
