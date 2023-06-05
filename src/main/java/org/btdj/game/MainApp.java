package org.btdj.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.btdj.game.Components.TowerComponent;
import org.btdj.game.Factories.BloonFactory;
import org.btdj.game.Logic.RoundHandler;

import java.util.ArrayList;

public class MainApp extends GameApplication {
    public static final Point2D BLOON_SPAWN = new Point2D(-50,300);
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
        settings.setVersion("PRE ALPHA 0.2");
    }

    public static final ArrayList<Entity> bloonList = new ArrayList<>();

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BloonFactory());

        new RoundHandler(1).start();

        Entity tower = FXGL.entityBuilder()
                .at(500, 200)
                .view(new Rectangle(25, 25, Color.BLUE))
                .type(EntityType.TOWER)
                .buildAndAttach();

        tower.setRotationOrigin(new Point2D(25.0/2,25.0/2));
        Rectangle collider = new Rectangle(400+12.5, 100+12.5, 200, 200);
        collider.setOpacity(0.2);
        FXGL.getGameScene().addChild(collider);
        tower.addComponent(new TowerComponent());

        //FXGL.play("music.wav");
    }

    public static void main(String[] args) {
        launch(args);
    }
}