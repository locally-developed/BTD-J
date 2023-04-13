package org.btdj.game;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
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
        for (int i = 0; i < 5; i++) {
            bloonList.add(
                    FXGL.entityBuilder()
                            .at(300 - i*75,300)
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

        for (Entity bloon: bloonList) {
            bloon.getComponent(BloonsComponent.class).setVelocity(new Point2D(3,0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}