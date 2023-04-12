package org.btdj.game;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.Components.TowerComponent;

import java.util.ArrayList;

public class MainApp extends GameApplication {
    public enum EntityType {
        TOWER,
        BLOON,
        PROJECTILE
    }
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("BTD-J");
        settings.setVersion("PRE ALPHA 0.1");
    }

    private final ArrayList<Entity> bloonList = new ArrayList<>();
    private Entity tower;

    @Override
    protected void initGame() {
        for (int i = 0; i < 2; i++) {
            bloonList.add(
                    FXGL.entityBuilder()
                            .at(300 - i*50,300)
                            .view("bloons/blue.png")
                            .type(EntityType.BLOON)
                            .buildAndAttach()
            );
            bloonList.get(i).addComponent(new BloonsComponent());
            bloonList.get(i).setProperty("order", i);
        }

        tower = FXGL.entityBuilder()
                .at(500, 200)
                .view(new Rectangle(25, 25, Color.BLUE))
                .type(EntityType.TOWER)
                .buildAndAttach();

        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));
        Rectangle collider = new Rectangle(400+12.5, 100+12.5, 200, 200);
        collider.setOpacity(0.2);
        FXGL.getGameScene().addChild(collider);
        tower.addComponent(new TowerComponent(bloonList));

        FXGL.play("music.wav");

        Path track = new Path(
                new MoveTo(300,300),
                new LineTo(600, 300),
                new LineTo(600, 600),
                new LineTo(800, 600)
        );

        FXGL.animationBuilder()
                .interpolator(Interpolators.LINEAR.EASE_IN_OUT())
                .onFinished(() -> bloonList.get(0).getComponent(BloonsComponent.class).pop())
                .duration(Duration.seconds(5))
                .translate(bloonList.get(0))
                .alongPath(track)
                .buildAndPlay();
    }

    public static void main(String[] args) {
        launch(args);
    }
}