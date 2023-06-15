package org.btdj.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.MouseTrigger;
import com.almasb.fxgl.input.TriggerListener;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import org.btdj.game.Components.*;
import org.btdj.game.Factories.BloonFactory;
import org.btdj.game.Factories.DartFactory;
import org.btdj.game.Factories.TowerFactory;
import org.btdj.game.Logic.RoundHandler;
import org.btdj.game.Logic.WaypointHandler;
import org.btdj.game.UI.ButtonCreator;
import org.btdj.game.UI.TextCreator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainApp extends GameApplication {
    public static final Point2D BLOON_SPAWN = new Point2D(-50,425);

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
    public static int gameMoney = 650;
    private static int gameRound = 0;
    public static int globalSpeedModifier = 1;
    private static boolean isRoundActive = false;

    private RoundHandler currentRound;
    public static Entity towerPlacer;
    public static Text healthDisplay;
    public static Text moneyDisplay;

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BloonFactory());
        FXGL.getGameWorld().addEntityFactory(new TowerFactory());
        FXGL.getGameWorld().addEntityFactory(new DartFactory());

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
        Group dartMonkeyButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-265, 200),
                "ui/towerPortraits/dartMonkey/default.png",
                0.5
        );

        dartMonkeyButton.setOnMouseClicked(e -> {
            towerPlacer = FXGL.entityBuilder()
                    .at(0,0)
                    .view(new Rectangle(50,50, Color.RED))
                    .buildAndAttach();
            towerPlacer.addComponent(new TowerPlaceComponent("dartMonkey"));
        });
        FXGL.getGameScene().addUINode(dartMonkeyButton);

        Group tackShooterButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-135, 200),
                "ui/towerPortraits/tackShooter/default.png",
                -0.25
        );

        tackShooterButton.setOnMouseClicked(e -> {
            towerPlacer = FXGL.entityBuilder()
                    .at(0,0)
                    .view(new Rectangle(50,50, Color.RED))
                    .buildAndAttach();
            towerPlacer.addComponent(new TowerPlaceComponent("tackShooter"));
        });
        FXGL.getGameScene().addUINode(tackShooterButton);

        Group bombShooterButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-265, 350),
                "ui/towerPortraits/bombShooter/default.png",
                -0.5
        );

        bombShooterButton.setOnMouseClicked(e -> {
            towerPlacer = FXGL.entityBuilder()
                    .at(0,0)
                    .view(new Rectangle(50,50, Color.RED))
                    .buildAndAttach();
            towerPlacer.addComponent(new TowerPlaceComponent("bombShooter"));
        });
        FXGL.getGameScene().addUINode(bombShooterButton);

        ImageView playButton = new ImageView(FXGL.getAssetLoader().loadImage("ui/button_play.png"));
        playButton.setX(FXGL.getSettings().getWidth()-275);
        playButton.setY(900);
        ButtonCreator.hoverEffect(playButton, new ColorAdjust());
        playButton.setOnMouseClicked(e -> {
            if (!isRoundActive) {
                isRoundActive = true;
                currentRound = new RoundHandler(++gameRound);
                currentRound.start();
            }
        });
        FXGL.getGameScene().addUINode(playButton);

        ImageView boostButton = new ImageView(FXGL.getAssetLoader().loadImage("ui/button_skip.png"));
        boostButton.setX(FXGL.getSettings().getWidth()-125);
        boostButton.setY(900);
        ButtonCreator.hoverEffect(boostButton, new ColorAdjust());
        boostButton.setOnMouseClicked(e -> {
            globalSpeedModifier = globalSpeedModifier == 1 ? 2 : 1;
            currentRound.doubleTime = globalSpeedModifier == 2;
        });
        FXGL.getGameScene().addUINode(boostButton);

        healthDisplay = TextCreator.create(String.valueOf(gameHealth));
        healthDisplay.setX(50);
        healthDisplay.setY(50);

        moneyDisplay = TextCreator.create(String.valueOf(gameMoney));
        moneyDisplay.setX(200);
        moneyDisplay.setY(50);

        FXGL.getGameScene().addUINodes(healthDisplay, moneyDisplay);
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

        addMoney(100+gameRound);
        isRoundActive = false;
    }

    public static void addMoney(int money) {
        gameMoney += money;
        moneyDisplay.setText(String.valueOf(gameMoney));
    }
    public static void removeHealth(int health) {
        gameHealth -= health;
        healthDisplay.setText(String.valueOf(gameHealth));
    }

    public static void main(String[] args) {
        launch(args);
    }
}