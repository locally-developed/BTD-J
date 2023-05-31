package org.btdj.game.Components;

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

public class TowerComponent extends Component {
    public enum Priority {
        FIRST,
        LAST,
        STRONGEST,
        WEAKEST
    }

    private final GameWorld world = FXGL.getGameWorld();
    private final Rectangle2D rangeCollider;
    private float coolDown = 0;
    private Priority targetingPriority = Priority.FIRST;
    public TowerComponent(ArrayList<Entity> bloonList) {
        this.rangeCollider = new Rectangle2D(400, 100, 200, 200);
    }

    @Override
    public void onUpdate(double tpf) {
        List<Entity> bloonsInRange = world.getEntitiesInRange(rangeCollider);

        switch (targetingPriority) {
            case FIRST:
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == MainApp.EntityType.BLOON)
                        .toList();
                break;
            case STRONGEST:
                Comparator.comparing()
                break;
        }


        if (!bloonsInRange.isEmpty()) {
            Entity target = bloonsInRange.get(0);

            Point2D p1 = target.getPosition().subtract(entity.getPosition());
            Point2D lookVector = new Point2D(1, 0);
            double angle = lookVector.angle(p1);

            entity.setRotation(angle);

            if (coolDown >= 1) {
                target.getComponent(BloonsComponent.class).pop();
                coolDown = 0;
            }
        }
        coolDown += tpf;
    }

    public void setTargetingPriority(Priority priority) {
        targetingPriority = priority;
    }
}
