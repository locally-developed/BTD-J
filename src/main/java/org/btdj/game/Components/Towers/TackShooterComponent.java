package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.Components.DartComponent;
import org.btdj.game.MainApp;


public class TackShooterComponent extends TowerComponent {
    private double coolDown = 1;
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
    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1.4) {
            super.radiiTower(entities -> {
                for(Point2D velocity: dartVectors) {
                    Entity dart = FXGL.getGameWorld().spawn("dart");
                    dart.setPosition(entity.getPosition().add(new Point2D(12.5,12.5)));
                    dart.setRotation(new Point2D(0,1).angle(velocity));
                    dart.getComponent(DartComponent.class).setVelocity(velocity);
                }
                coolDown = 0;
            });
        }

        coolDown += tpf * MainApp.globalSpeedModifier;
    }
    public void setRangeCollider(Rectangle2D collider) {
        this.rangeCollider = collider;
    }
}
