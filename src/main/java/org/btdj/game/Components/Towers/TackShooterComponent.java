package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.Components.DartComponent;
import org.btdj.game.EntityType;

import java.util.List;

public class TackShooterComponent extends Component {
    private float coolDown = 1;
    private Rectangle2D rangeCollider;
    private final Point2D[] dartVectors = {
            new Point2D(0,-7),
            new Point2D(7,-7),
            new Point2D(7,0),
            new Point2D(7,7),
            new Point2D(0,7),
            new Point2D(-7,7),
            new Point2D(-7,0),
            new Point2D(-7,-7)
    };

    /*public TackShooterComponent() {
        this.rangeCollider = new Rectangle2D(
                entity.getX()-100,
                entity.getY()-100,
                200,
                200
        );
    }*/
    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1.4) {
            List<Entity> bloonsInRange = FXGL.getGameWorld().getEntitiesInRange(rangeCollider)
                    .stream()
                    .filter(e -> e.getType() == EntityType.BLOON)
                    .toList();
            if (bloonsInRange.isEmpty()) return;

            for(Point2D velocity: dartVectors) {
                Entity dart = FXGL.getGameWorld().spawn("dart");
                dart.setPosition(entity.getPosition().add(new Point2D(12.5,12.5)));
                dart.setRotation(new Point2D(0,1).angle(velocity));
                dart.getComponent(DartComponent.class).setVelocity(velocity);
            }
            coolDown = 0;
            return;
        }

        coolDown += tpf;
    }
    public void setRangeCollider(Rectangle2D collider) {
        this.rangeCollider = collider;
    }
}
