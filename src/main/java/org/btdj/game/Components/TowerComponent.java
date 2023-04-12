package org.btdj.game.Components;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.MainApp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class TowerComponent extends Component {
    public enum Priority {
        FIRST,
        LAST,
        STRONGEST,
        WEAKEST
    }

    private final GameWorld world = FXGL.getGameWorld();
    private Rectangle2D rangeCollider;
    private Priority targetingPriority = Priority.FIRST;
    public TowerComponent(ArrayList<Entity> bloonList) {
        this.rangeCollider = new Rectangle2D(400, 100, 200, 200);
    }

    @Override
    public void onUpdate(double tpf) {
        List<Entity> bloonsInRange = world.getEntitiesInRange(rangeCollider)
                .stream()
                .filter(e -> e.getType() == MainApp.EntityType.BLOON)
                .toList();

        if (!bloonsInRange.isEmpty()) {
            Entity target = bloonsInRange.get(0);

            Point2D p1 = target.getPosition().subtract(entity.getPosition());
            Point2D lookVector = new Point2D(1, 0);
            double angle = lookVector.angle(p1);

            entity.setRotation(angle);
            target.getComponent(BloonsComponent.class).pop(1);
        }
    }

    public void setTargetingPriority(Priority priority) {
        targetingPriority = priority;
    }
}
