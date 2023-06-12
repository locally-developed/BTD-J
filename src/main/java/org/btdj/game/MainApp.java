package org.btdj.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.LevelLoader;
import com.almasb.fxgl.input.MouseTrigger;
import com.almasb.fxgl.input.TriggerListener;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import org.btdj.game.Components.*;
import org.btdj.game.Factories.BloonFactory;
import org.btdj.game.Factories.TowerFactory;
import org.btdj.game.Logic.RoundHandler;
import org.btdj.game.Logic.WaypointHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class MainApp extends GameApplication {
    public static final Point2D BLOON_SPAWN = new Point2D(-50,425);
    public enum EntityType {
        TOWER,
        BLOON,
        //PROJECTILE
    }
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setIntroEnabled(false);
        settings.setHeight(1080);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setTitle("BTD-J");
        settings.setFontText("LuckiestGuy-Regular.ttf");
        settings.setFontUI("LuckiestGuy-Regular.ttf");
        settings.setVersion("PRE ALPHA 0.2");
    }

    public static final ArrayList<Entity> bloonList = new ArrayList<>();
    public static int gameHealth = 200;
    public static Entity towerPlacer;

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BloonFactory());
        FXGL.getGameWorld().addEntityFactory(new TowerFactory());

        new RoundHandler(5).start();

//        Entity tower = FXGL.getGameWorld().spawn("tower");
//        tower.setPosition(500, 200);

        Rectangle collider = new Rectangle(400+12.5, 100+12.5, 200, 200);
        collider.setOpacity(0.2);
        FXGL.getGameScene().addChild(collider);

        FXGL.entityBuilder()
                .view("ui/level.png")
                .zIndex(-1)
                .buildAndAttach();

        new WaypointHandler();

        FXGL.entityBuilder()
                .at(680, 1080)
                .buildAndAttach()
                .addComponent(new EndComponent());

        FXGL.play("music.wav");
    }

    @Override
    protected void initUI() {
        Rectangle button = new Rectangle(
                FXGL.getSettings().getWidth()-275,
                300,
                150,
                150
        );
        button.setOnMouseClicked(e -> {
            towerPlacer = FXGL.entityBuilder()
                    .at(0,0)
                    .view(new Rectangle(25,25, Color.RED))
                    .buildAndAttach();
            towerPlacer.addComponent(new TowerPlaceComponent());
        });
        button.setOnMouseEntered(e -> {
            button.
        });

        Text thing = new Text(String.valueOf(gameHealth));
        thing.setFont(FXGL.getAssetLoader().loadFont("LuckiestGuy-Regular.ttf").newFont(52));
        thing.setFill(Color.WHITE);
        thing.setStroke(Color.BLACK);
        thing.setStrokeWidth(3);
        thing.setStrokeType(StrokeType.OUTSIDE);
        thing.setStrokeLineJoin(StrokeLineJoin.ROUND);
        FXGL.entityBuilder()
                .at(50,50)
                .view(thing)
                .buildAndAttach()
                .addComponent(new ValueDisplayComponent());
        FXGL.getGameScene().addUINode(button);
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
                if (towerPlacer != null && towerPlacer.isActive()) {
                    towerPlacer.getComponent(TowerPlaceComponent.class).place();
                }
            }

            @Override
            protected void onButtonEnd(@NotNull MouseTrigger mouseTrigger) {
                super.onButtonEnd(mouseTrigger);
            }
        });
    }

    public static void removePlacer() {
        FXGL.getGameWorld().removeEntity(towerPlacer);
    }

    public static void main(String[] args) {
        launch(args);
    }
}