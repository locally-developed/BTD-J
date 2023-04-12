package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.MainApp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TowerComponent extends Component {
    public enum Priority {
        FIRST,
        LAST,
        STRONGEST,
        WEAKEST
    }

    private GameWorld world = FXGL.getGameWorld();
    private Rectangle2D rangeCollider;
    private Entity target;
    private Priority targetingPriority = Priority.FIRST;
    public TowerComponent(ArrayList<Entity> bloonList) {
        this.rangeCollider = new Rectangle2D(400, 100, 200, 200);
        this.target = bloonList.get(
                switch(targetingPriority) {
                    case FIRST:
                        yield 0;
                    case LAST:
                        yield bloonList.size() - 1;
                    case STRONGEST:
                        yield 1;
                    case WEAKEST:
                        yield 2;
                }
        );
    }

    @Override
    public void onUpdate(double tpf) {
        List<Entity> bloonsInRange = world.getEntitiesInRange(rangeCollider)
                .stream()
                .filter(e -> e.getType() == MainApp.EntityType.BLOON)
                .toList();
        if (bloonsInRange.size() > 0) {
            target = bloonsInRange.get(0);

            Point2D p1 = target.getPosition().subtract(entity.getPosition());
            Point2D lookVector = new Point2D(1, 0);
            double angle = lookVector.angle(p1);

            entity.setRotation(angle);

        }
    }

    public void setTargetingPriority(Priority priority) {
        targetingPriority = priority;
    }
}
