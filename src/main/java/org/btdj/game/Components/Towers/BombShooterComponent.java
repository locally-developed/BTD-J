package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;

import java.util.List;

public class BombShooterComponent extends TowerComponent{
    private final GameWorld world = FXGL.getGameWorld();
    private double coolDown = 1.5;
    private final AnimatedTexture explosion = new AnimatedTexture(new AnimationChannel(
          FXGL.getAssetLoader().loadImage("explosion.png"),
          5,
          150,
          150,
            Duration.seconds(0.5),
            0,
            24
    ));
    private final Entity explosionObject = FXGL.entityBuilder()
            .at(-150,-150)
            .zIndex(10)
            .view(explosion)
            .buildAndAttach();

    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1.5) {
            super.trackTower((target) -> {
                explosionObject.setPosition(target.getPosition().subtract(new Point2D(10, 20)));
                explosion.playFrom(0);
                List<Entity> bloonsInRange = world.getEntitiesInRange(new Rectangle2D(
                        target.getX()-35,
                        target.getY()-35,
                        70,
                        70
                )).stream().filter(e -> e.getType() == EntityType.BLOON).toList();
                for (Entity bloon: bloonsInRange) {
                    bloon.getComponent(BloonsComponent.class).pop();
                }

                FXGL.play("boom.wav");
                coolDown = 0;
            });
        }
        coolDown += tpf * MainApp.globalSpeedModifier;
    }
}
