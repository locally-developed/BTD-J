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

/**
 * Bloons Tower Defence but in Java, because why not?!
 *
 * Created for Mr. Smithe's Grade 12 Computer Science final project.
 *
 *
 * Dear Mr. Smithe,
 *
 * Thank you so much for teaching me this wonderful language. I honestly couldn't
 * have learnt it myself (mostly because I would've given up after a week). I hope
 * you continue teaching others who have a passion for programming, as I know you
 * do, which is why you're so damn good at teaching it. Hope to stay in touch, let
 * me know how your students are doing, and whether another Noah Freelove shows up!
 * Thanks again for the 3 years.
 *
 * Sincerely,
 * @author Kasper Pajak
 *
 * @version 1.0
 */
public class MainApp extends GameApplication {
    public static final Point2D BLOON_SPAWN = new Point2D(-50,400); //Point at which Bloons will spawn on the map

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
    public static int gameHealth = 200; //Initial health (bloons you can lose)
    public static int gameMoney = 1650; //Initial money
    public static int gameXP = 0;   //Reserved for future use
    private static int gameRound = 0;   //Which round is currently being played. Increments prior to round start.
    public static int globalSpeedModifier = 1;  //Multiplier used by all entities to determine fast-forward rate
    private static boolean isRoundActive = false;   //Debounce

    private RoundHandler currentRound;
    public static Entity towerPlacer;
    public static Text towerPlaceHint;
    private static int towerPlaceCost = 0;  //The cost of placing down the currently-selected tower
    public static Text healthDisplay;
    public static Text moneyDisplay;

    @Override
    protected void initGame() {
        //Initializes and attaches all the entity factories
        FXGL.getGameWorld().addEntityFactory(new BloonFactory());
        FXGL.getGameWorld().addEntityFactory(new TowerFactory());
        FXGL.getGameWorld().addEntityFactory(new DartFactory());

        FXGL.entityBuilder()    //Game background
                .view("ui/level.png")
                .zIndex(-1)
                .buildAndAttach();

        new WaypointHandler();  //Creates the waypoints (turning points along the track)

        FXGL.entityBuilder()    //Collider at the end of track which deletes un-popped bloons
                .at(680, 1080)
                .buildAndAttach()
                .addComponent(new EndComponent());

        FXGL.play("music.wav"); //Starts background music
    }

    @Override
    protected void initUI() {
        Group dartMonkeyButton = ButtonCreator.create(  //UI button creator (tower shop)
                new Point2D(FXGL.getSettings().getWidth()-265, 200),
                "ui/towerPortraits/dartMonkey/default.png",
                0.5,
                170
        );
        //Connecting the button press event with createTowerPlacer, passing the tower name and cost to place
        dartMonkeyButton.setOnMouseClicked(e -> createTowerPlacer("dartMonkey", 170));
        FXGL.getGameScene().addUINode(dartMonkeyButton);    //Adding the button to the game scene

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

        //Creating the "start round" button, using the game engine asset loader to get the button image
        ImageView playButton = new ImageView(FXGL.getAssetLoader().loadImage("ui/button_play.png"));
        playButton.setX(FXGL.getSettings().getWidth()-275); //Positioning it within the UI
        playButton.setY(900);
        ButtonCreator.hoverEffect(playButton, new ColorAdjust());   //Applying a hover-over effect to the button
        playButton.setOnMouseClicked(e -> {
            if (!isRoundActive) {   //Check debounce
                isRoundActive = true;   //Set debounce
                currentRound = new RoundHandler(++gameRound);   //Creating a new round
                currentRound.start();   //Starting the round
            }
        });
        FXGL.getGameScene().addUINode(playButton);

        ImageView boostButton = new ImageView(FXGL.getAssetLoader().loadImage("ui/button_skip.png"));
        boostButton.setX(FXGL.getSettings().getWidth()-125);
        boostButton.setY(900);
        ButtonCreator.hoverEffect(boostButton, new ColorAdjust());
        //Flip-flops the globalSpeedModifier between x1 and x2 whenever the boost button is pressed
        boostButton.setOnMouseClicked(e -> globalSpeedModifier = globalSpeedModifier == 1 ? 2 : 1);
        FXGL.getGameScene().addUINode(boostButton);

        healthDisplay = TextCreator.create(String.valueOf(gameHealth)); //Using text creator to make stylized text
        healthDisplay.setX(50);
        healthDisplay.setY(50);

        moneyDisplay = TextCreator.create(String.format("$%s", gameMoney));
        moneyDisplay.setX(250);
        moneyDisplay.setY(50);

        towerPlaceHint = TextCreator.create("Right click to cancel");   //Tower place hint
        towerPlaceHint.setX(600);
        towerPlaceHint.setY(1000);
        towerPlaceHint.setVisible(false);   //Setting it to invisible until a tower is being placed

        FXGL.getGameScene().addUINodes(healthDisplay, moneyDisplay, towerPlaceHint);
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addTriggerListener(new TriggerListener() {
            //As onButtonBegin is called by TriggerListener whenever a mouse button is pressed, we override it to wrap
            //with the tower place method, plus some checksums.
            @Override
            protected void onButtonBegin(@NotNull MouseTrigger mouseTrigger) {
                super.onButtonBegin(mouseTrigger);  //Internal game engine call to other methods that rely on mouseInput
                switch (mouseTrigger.getButton()) { //Differentiating different button presses
                    case PRIMARY -> {   //Left Mouse Button (Button 1)
                        //Left-click to place the tower
                        if (towerPlacer != null) {  //Check if tower is actively being placed
                            towerPlacer.getComponent(TowerPlaceComponent.class).place();
                        }
                    }
                    case SECONDARY -> { //Right Mouse Button (Button 3)
                        //Right-click to cancel tower placement
                        if (towerPlacer != null) {
                            towerPlacer.removeFromWorld();  //Removing the tower-placer entity
                            towerPlacer = null;
                            towerPlaceHint.setVisible(false);   //Hiding the tower-placer hint
                        }
                    }
                }
            }
        });
    }

    /**
     * Creates a new tower-placing entity
     *
     * @param tower name of the tower
     * @param cost cost to place the tower
     */
    private static void createTowerPlacer(String tower, int cost) {
        if (towerPlacer != null) return;    //Exit if tower is already being placed
        if (gameMoney < cost) return;   //Check if player can afford to place the tower

        towerPlaceCost = cost;  //Storing the cost for later since we only want to subtract the money upon placement
        towerPlaceHint.setVisible(true);    //Showing the tower-place hint

        Circle hitBoxThing = new Circle(25, 25, 150);   //Circle representing the tower range
        hitBoxThing.setOpacity(0.2);
        FXGL.getGameScene().addChild(hitBoxThing);

        towerPlacer = FXGL.entityBuilder()  //Creates a new tower-placer entity
                .at(0,0)
                .view(new Rectangle(50,50, Color.RED))
                .buildAndAttach();
        towerPlacer.getViewComponent().addChild(hitBoxThing);   //Adding the range circle to the tower-placer entity
        towerPlacer.addComponent(new TowerPlaceComponent(tower));   //Attaching the TowerPlaceComponent
    }

    /**
     * Removes the active tower-placer entity from the game
     */
    public static void removePlacer() {
        FXGL.getGameWorld().removeEntity(towerPlacer);
        towerPlacer = null;
        addMoney(-towerPlaceCost);
        towerPlaceCost = 0;
        towerPlaceHint.setVisible(false);
    }

    /**
     * Post-round logic and cleanup
     */
    public static void declareRoundComplete() {
        if (gameHealth <= 0) {  //Checks if the player lost the round (ran out of health)
            System.exit(0); //Ran out of time, so the program just shoots itself in the foot if you lose
            return; //Probably redundant, but good to have anyway
        }

        addMoney(100+gameRound);    //Round completion bonus
        isRoundActive = false;  //Setting debounce
    }

    /**
     * Gives money to the player and updates visual representation
     *
     * @param money amount of money to add
     */
    public static void addMoney(int money) {
        gameMoney += money;
        moneyDisplay.setText(String.valueOf(String.format("$%s", gameMoney)));
    }
    /**
     * Damages the player and updates visual representation
     *
     * @param health amount of damage (bloons)
     */
    public static void removeHealth(int health) {
        gameHealth -= health;
        healthDisplay.setText(String.valueOf(gameHealth));
    }

    public static void main(String[] args) {
        launch(args);   //Where the magic happens
    }
}