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
import javafx.scene.text.Text;
import org.btdj.game.Components.*;
import org.btdj.game.Factories.BloonFactory;
import org.btdj.game.Factories.TowerFactory;
import org.btdj.game.Logic.RoundHandler;
import org.btdj.game.Logic.WaypointHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    public static int gameMoney = 300;
    private static int gameRound = 0;
    public static int globalSpeedModifier = 1;
    private static boolean isRoundActive = false;

    private RoundHandler currentRound;
    public static Entity towerPlacer;

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BloonFactory());
        FXGL.getGameWorld().addEntityFactory(new TowerFactory());

        FXGL.entityBuilder()
                .view("ui/level.png")
                .zIndex(-1)
                .buildAndAttach();

        new WaypointHandler();

        FXGL.entityBuilder()
                .at(680, 1080)
                .buildAndAttach()
                .addComponent(new EndComponent());

        //FXGL.play("music.wav");
    }

    @Override
    protected void initUI() {
        Rectangle button = new Rectangle(
                FXGL.getSettings().getWidth()-275,
                300,
                150,
                150
        );
        new ButtonComponent(button, Color.LIGHTGRAY, Color.GRAY);
        button.setOnMouseClicked(e -> {
            towerPlacer = FXGL.entityBuilder()
                    .at(0,0)
                    .view(new Rectangle(25,25, Color.RED))
                    .buildAndAttach();
            towerPlacer.addComponent(new TowerPlaceComponent());
        });
        FXGL.getGameScene().addUINode(button);

        Rectangle playButton = new Rectangle(
                FXGL.getSettings().getWidth()-275,
                900,
                100,
                100
        );
        new ButtonComponent(playButton, Color.GREEN, Color.DARKGREEN);
        playButton.setOnMouseClicked(e -> {
            if (!isRoundActive) {
                isRoundActive = true;
                currentRound = new RoundHandler(++gameRound);
                currentRound.start();
            }
        });
        FXGL.getGameScene().addUINode(playButton);

        Rectangle boostButton = new Rectangle(
                FXGL.getSettings().getWidth()-150,
                900,
                100,
                100
        );
        new ButtonComponent(boostButton, Color.YELLOW, Color.DARKKHAKI);
        boostButton.setOnMouseClicked(e -> {
            globalSpeedModifier = globalSpeedModifier == 1 ? 2 : 1;
            currentRound.doubleTime = globalSpeedModifier == 2 ? true : false;
        });
        FXGL.getGameScene().addUINode(boostButton);



        Text thing = new Text();
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
                .addComponent(new HealthDisplayComponent());
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
    public static void declareRoundComplete() {
        if (gameHealth <= 0) {
            System.exit(0);
            return;
        }

        isRoundActive = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}