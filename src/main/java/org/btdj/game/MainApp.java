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
    public static final Point2D BLOON_SPAWN = new Point2D(-50,400);

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
    public static int gameMoney = 1650;
    public static int gameXP = 0;
    private static int gameRound = 30;
    public static int globalSpeedModifier = 1;
    private static boolean isRoundActive = false;

    private RoundHandler currentRound;
    public static Entity towerPlacer;
    public static Text towerPlaceHint;
    private static int towerPlaceCost = 0;
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

        FXGL.play("music.wav");
    }

    @Override
    protected void initUI() {
        Group dartMonkeyButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-265, 200),
                "ui/towerPortraits/dartMonkey/default.png",
                0.5,
                170
        );
        dartMonkeyButton.setOnMouseClicked(e -> createTowerPlacer("dartMonkey", 170));
        FXGL.getGameScene().addUINode(dartMonkeyButton);

        Group tackShooterButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-135, 200),
                "ui/towerPortraits/tackShooter/default.png",
                -0.25,
                240
        );
        tackShooterButton.setOnMouseClicked(e -> createTowerPlacer("tackShooter", 240));
        FXGL.getGameScene().addUINode(tackShooterButton);

        Group bombShooterButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-265, 350),
                "ui/towerPortraits/bombShooter/default.png",
                -0.5,
                445
        );
        bombShooterButton.setOnMouseClicked(e -> createTowerPlacer("bombShooter", 445));
        FXGL.getGameScene().addUINode(bombShooterButton);

        Group glueGunnerButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-135, 350),
                "ui/towerPortraits/glueGunner/default.png",
                -0.75,
                190
        );
        glueGunnerButton.setOnMouseClicked(e -> createTowerPlacer("glueGunner", 190));
        FXGL.getGameScene().addUINode(glueGunnerButton);

        Group dartlingGunnerButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-265, 500),
                "ui/towerPortraits/dartlingGunner/default.png",
                0,
                720
        );
        dartlingGunnerButton.setOnMouseClicked(e -> createTowerPlacer("dartlingGunner", 720));
        FXGL.getGameScene().addUINode(dartlingGunnerButton);

        Group iceMonkeyButton = ButtonCreator.create(
                new Point2D(FXGL.getSettings().getWidth()-135, 500),
                "ui/towerPortraits/iceMonkey/default.png",
                0.15,
                425
        );
        iceMonkeyButton.setOnMouseClicked(e -> createTowerPlacer("iceMonkey", 425));
        FXGL.getGameScene().addUINode(iceMonkeyButton);

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
        boostButton.setOnMouseClicked(e -> globalSpeedModifier = globalSpeedModifier == 1 ? 2 : 1);
        FXGL.getGameScene().addUINode(boostButton);

        healthDisplay = TextCreator.create(String.valueOf(gameHealth));
        healthDisplay.setX(50);
        healthDisplay.setY(50);

        moneyDisplay = TextCreator.create(String.format("$%s", gameMoney));
        moneyDisplay.setX(250);
        moneyDisplay.setY(50);

        towerPlaceHint = TextCreator.create("Right click to cancel");
        towerPlaceHint.setX(600);
        towerPlaceHint.setY(1000);
        towerPlaceHint.setVisible(false);

        FXGL.getGameScene().addUINodes(healthDisplay, moneyDisplay, towerPlaceHint);
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
                switch (mouseTrigger.getButton()) {
                    case PRIMARY -> {
                        if (towerPlacer != null) {
                            towerPlacer.getComponent(TowerPlaceComponent.class).place();
                        }
                    }
                    case SECONDARY -> {
                        if (towerPlacer != null) {
                            towerPlacer.removeFromWorld();
                            towerPlacer = null;
                            towerPlaceHint.setVisible(false);
                        }
                    }
                }

            }

            @Override
            protected void onButtonEnd(@NotNull MouseTrigger mouseTrigger) {
                super.onButtonEnd(mouseTrigger);
            }
        });
    }
    private static void createTowerPlacer(String tower, int cost) {
        if (towerPlacer != null) return;
        if (gameMoney < cost) return;

        towerPlaceCost = cost;
        towerPlaceHint.setVisible(true);

        Circle hitboxThing = new Circle(25, 25, 150);
        hitboxThing.setOpacity(0.2);
        FXGL.getGameScene().addChild(hitboxThing);

        towerPlacer = FXGL.entityBuilder()
                .at(0,0)
                .view(new Rectangle(50,50, Color.RED))
                .buildAndAttach();
        towerPlacer.getViewComponent().addChild(hitboxThing);
        towerPlacer.addComponent(new TowerPlaceComponent(tower));
    }
    public static void removePlacer() {
        FXGL.getGameWorld().removeEntity(towerPlacer);
        towerPlacer = null;
        addMoney(-towerPlaceCost);
        towerPlaceCost = 0;
        towerPlaceHint.setVisible(false);
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
        moneyDisplay.setText(String.valueOf(String.format("$%s", gameMoney)));
    }
    public static void removeHealth(int health) {
        gameHealth -= health;
        healthDisplay.setText(String.valueOf(gameHealth));
    }

    public static void main(String[] args) {
        launch(args);
    }
}