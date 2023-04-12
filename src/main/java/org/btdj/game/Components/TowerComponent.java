package org.btdj.game.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class TowerComponent extends Component {
    public enum Priority {
        FIRST,
        LAST,
        STRONGEST,
        WEAKEST
    }

    private Entity target;
    private Priority targetingPriority = Priority.FIRST;
    public TowerComponent(ArrayList<Entity> bloonList) {
        target = bloonList.get(
                switch(targetingPriority) {
                    case FIRST:
                        yield 0;
                    case LAST:
                        yield bloonList.size() - 1;
                    case STRONGEST:
                        yield 0;
                    case WEAKEST:
                        yield 0;
                }
        );
    }

    @Override
    public void onUpdate(double tpf) {
        if (target != null) {
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
