package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.btdj.game.MainApp;
import org.btdj.game.Util.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles the bloon functionality
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class BloonsComponent extends Component {
    private Point2D velocity = new Point2D(1,0);    //Unit vector velocity of the bloon
    private JsonNode properties;    //Stores the bloon's properties in JSON
    private int health; //Bloon health (only used for ceramic bloons)
    public double speedMultiplier = 1;  //Speed multiplier for speed-affecting towers (glue gunner)
    private double speed;   //Speed of the bloon (default: 1)
    private int RBE;    //Red Bloon Equivalent (how many red bloons are potentially inside this bloon)
    public String bloonType;
    private String initialType; //What the bloon initially started as (for regrowth bloons)
    private String bloonVariant = "default";
    public boolean glued = false;   //Whether the bloon has been covered in glue
    public int frozenSpeedMod = 1;  //Speed modifier for frozen bloons (separate because this value becomes zero)
    private final ArrayList<BloonModifier> modifiers = new ArrayList<>();   //Contains bloon modifiers
    private TimerAction regrowLoop; //Loop for regrowing the bloon
    private int processDelay = 0;   //Same purpose as in the RoundHandler class

    @Override
    public void onUpdate(double tpf) {  //time per frame
        entity.translate(this.velocity.multiply(2 * speed * speedMultiplier * frozenSpeedMod * MainApp.globalSpeedModifier));
        //Moves the bloon every frame in a certain direction, multiplied by any influencing factors
    }

    /**
     * Gets the properties of a specified bloon, parses them, and updates the bloon's behavior/appearance.
     * Used for first-time bloon setup
     *
     * @param type bloon type
     * @param passedMods list of bloon modifiers
     */
    public void updateProperties(String type, ArrayList<BloonModifier> passedMods) {
        try {
            properties = JsonParser.getBloonFromJson(type); //Getting the bloon data
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.bloonType = type;
        this.initialType = type;
        //Since this is the method called upon construction of this class, this is the initial bloon

        modifiers.addAll(passedMods);   //Adds the passed modifiers to the bloon's internal modifier registry

        //Parsing the bloon modifiers
        if (passedMods.contains(BloonModifier.CAMO)) bloonVariant = "camo";
        if (passedMods.contains(BloonModifier.CAMO) && passedMods.contains(BloonModifier.REGROW)) bloonVariant = "camoRegrow";
        if (passedMods.contains(BloonModifier.REGROW)) {
            bloonVariant = "regrow";

            //Sets up the regrowth loop for the bloon. The bloon will regrow one layer every 3 seconds until it reaches
            //its initial layer.
            regrowLoop = FXGL.getGameTimer().runAtInterval(() -> {
                if (processDelay > 0 || MainApp.globalSpeedModifier > 1) {  //Same thing as in RoundHandler
                    //Check if we're not at our initial bloon layer yet, then updating the bloon with the next layer
                    if (!Objects.equals(bloonType, initialType)) updateProperties(properties.get("parent").textValue());
                } else {
                    processDelay++;
                }
            }, Duration.seconds(1.5));
        }

        updateProperties(); //Calling the over base method to finish parsing the bloon properties
    }

    /**
     * Gets the properties of a specified bloon, parses them, and updates the bloon's behavior/appearance.
     * Used for concurrent bloon updating after the initial setup
     *
     * @param type bloon type
     */
    public void updateProperties(String type) {
        try {
            properties = JsonParser.getBloonFromJson(type);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.bloonType = type;
        updateProperties();
    }

    /**
     * Parses and updates the bloon's behavior/appearance based upon the stored bloon properties.
     */
    private void updateProperties() {
        this.health = properties.get("health").asInt(); //Parses bloon health
        this.RBE = properties.get("RBE").asInt(); //Parses RBE
        this.speed = properties.get("speed").asDouble();    //Parses bloon speed

        for (int i = 0; i < properties.get("tags").size(); i++) {   //Iterates over any non-meta modifiers
            JsonNode tag = properties.get("tags").get(i);
            modifiers.add(BloonModifier.valueOf(tag.asText())); //Adds non-meta modifiers to the bloon registry
        }

        //Setting up the bloon texture
        Texture image;
        if (bloonType.equals("ceramic")) {//If the bloon is ceramic, we'll apply a different texture based on its health
            image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%s/%s%d.png",
                    bloonType,
                    bloonVariant,
                    Math.abs((int) Math.ceil(health/2.0) - 5)
                    //Converting 10 -> 0 health into 0 -> 4 image variants
                    ));
        } else {
            image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%s/%s.png", bloonType, bloonVariant));
            //Used to get the image for a generic bloon
        }

        //Resizing the bloons
        image.setScaleX(0.5);
        image.setScaleY(0.5);
        image.setCache(true);   //Magic lag-reducing method (jk used to prevent JavaFX from updating it every frame)
        entity.getViewComponent().clearChildren();  //Removing the old bloon texture
        entity.getViewComponent().addChild(image);  //Adding the new bloon texture
    }

    /**
     * Handles bloon popping, verifying several checksums and conditions
     */
    public void pop() {
        FXGL.play("pop.wav");   //Plays the pop sound
        MainApp.addMoney(1);    //Awards the player money for popping the bloon
        if (health > 1) {   //If the bloon still has health left
            health--;   //Decrease the bloon's health point

            //Creating up the new image variant
            Texture image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%s/%s%d.png",
                    bloonType,
                    bloonVariant,
                    Math.abs((int) Math.ceil(health/2.0) - 5)
            ));
            image.setScaleX(0.5);
            image.setScaleY(0.5);
            image.setCache(true);
            entity.getViewComponent().clearChildren();
            entity.getViewComponent().addChild(image);

            return;
        }

        MainApp.gameXP++;   //Adding XP (unused)

        if (properties.get("child").size() == 0) {  //Removing the bloon if it's last in the chain
            remove();
            return;
        }
        if (properties.get("child").size() > 1) {   //If a bloon creates multiple bloons after popping, this handle that
            for (int i = 1; i < properties.get("child").size(); i++) {  //For each subsequent bloon...
                //Spawning in a new bloon at the current bloons position
                Entity bloon = FXGL.getGameWorld().spawn("bloon");
                bloon.setPosition(entity.getPosition().add(new Point2D(-10 * i, 0)));   //Eliminating stacking
                bloon.getComponent(BloonsComponent.class).updateProperties(properties.get("child").get(i).textValue());
                bloon.getComponent(BloonsComponent.class).setVelocity(new Point2D(
                        Math.signum(velocity.getX()),
                        Math.signum(velocity.getY())
                        ));
            }
        }

        updateProperties(properties.get("child").get(0).textValue());   //Updating the bloon once popped with a new look
    }

    /**
     * Removes the bloon from the game scene.
     */
    public void remove() {
        if (regrowLoop != null) regrowLoop.expire();    //Stops the regrowth loop if it exists
        FXGL.getGameWorld().removeEntity(entity);   //Removing the bloon
    }

    /**
     * Sets the bloon's travel velocity
     *
     * @param velocity velocity vector
     */
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the bloon's current travel velocity
     *
     * @return velocity vector
     */
    public Point2D getVelocity() {
        return this.velocity;
    }

    /**
     * Gets the bloon's RBE (red bloon equivalent)
     *
     * @return RBE
     */
    public int getRBE() {
        return this.RBE;
    }

    /**
     * Gets the bloon's non-meta modifiers
     *
     * @return modifiers
     */
    public ArrayList<BloonModifier> getModifiers() {
        return this.modifiers;
    }
}
