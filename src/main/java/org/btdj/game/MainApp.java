package org.btdj.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.MouseTrigger;
import com.almasb.fxgl.input.TriggerListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.btdj.game.Components.TowerComponent;
import org.btdj.game.Components.TowerPlaceComponent;
import org.btdj.game.Factories.BloonFactory;
import org.btdj.game.Logic.RoundHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainApp extends GameApplication {
    public static final Point2D BLOON_SPAWN = new Point2D(-50,300);
    public enum EntityType {
        TOWER,
        BLOON,
        //PROJECTILE
    }
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("BTD-J");
        settings.setVersion("PRE ALPHA 0.2");
    }

    public static final ArrayList<Entity> bloonList = new ArrayList<>();

    public static Entity towerPlacer;

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BloonFactory());

        new RoundHandler(1).start();

        Entity tower = FXGL.entityBuilder()
                .at(500, 200)
                .view(new Rectangle(25, 25, Color.BLUE))
                .type(EntityType.TOWER)
                .buildAndAttach();

        towerPlacer = FXGL.entityBuilder()
                .at(0,0)
                .view(new Rectangle(25,25, Color.RED))
                .buildAndAttach();
        towerPlacer.addComponent(new TowerPlaceComponent());

        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));
        Rectangle collider = new Rectangle(400+12.5, 100+12.5, 200, 200);
        collider.setOpacity(0.2);
        FXGL.getGameScene().addChild(collider);
        tower.addComponent(new TowerComponent());

        //FXGL.play("music.wav");
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addTriggerListener(new TriggerListener() {
            @Override
            protected void onButton(@NotNull MouseTrigger mouseTrigger) {
                super.onButton(mouseTrigger);
            }

            @Override
            protected void onButtonBegin(@NotNull MouseTrigger mouseTrigger) {
                super.onButtonBegin(mouseTrigger);
            }

            @Override
            protected void onButtonEnd(@NotNull MouseTrigger mouseTrigger) {
                super.onButtonEnd(mouseTrigger);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}