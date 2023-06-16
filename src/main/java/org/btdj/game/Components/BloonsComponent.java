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


public class BloonsComponent extends Component {
    private Point2D velocity = new Point2D(1,0);
    private JsonNode properties;
    private int health;
    public double speedMultiplier = 1;
    private double speed;
    private int RBE;
    public String bloonType;
    private String initialType;
    private String bloonVariant = "default";
    public boolean glued = false;
    public int frozenSpeedMod = 1;
    private final ArrayList<BloonModifier> modifiers = new ArrayList<>();
    private TimerAction regrowLoop;
    private int processDelay = 0;

    @Override
    public void onUpdate(double tpf) {
        entity.translate(this.velocity.multiply(2 * speed * speedMultiplier * frozenSpeedMod * MainApp.globalSpeedModifier));
    }

    public void updateProperties(String type, ArrayList<BloonModifier> passedMods) {
        try {
            properties = JsonParser.getBloonFromJson(type);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.bloonType = type;
        this.initialType = type;
        modifiers.addAll(passedMods);

        if (passedMods.contains(BloonModifier.CAMO)) bloonVariant = "camo";
        if (passedMods.contains(BloonModifier.CAMO) && passedMods.contains(BloonModifier.REGROW)) bloonVariant = "camoRegrow";
        if (passedMods.contains(BloonModifier.REGROW)) {
            bloonVariant = "regrow";

            regrowLoop = FXGL.getGameTimer().runAtInterval(() -> {
                if (processDelay > 0 || MainApp.globalSpeedModifier > 1) {
                    if (!Objects.equals(bloonType, initialType)) updateProperties(properties.get("parent").textValue());
                } else {
                    processDelay++;
                }
            }, Duration.seconds(1.5));
        }

        updateProperties();
    }
    public void updateProperties(String type) {
        try {
            properties = JsonParser.getBloonFromJson(type);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.bloonType = type;
        updateProperties();
    }
    private void updateProperties() {
        this.health = properties.get("health").asInt();
        this.RBE = properties.get("RBE").asInt();
        this.speed = properties.get("speed").asDouble();

        for (int i = 0; i < properties.get("tags").size(); i++) {
            JsonNode tag = properties.get("tags").get(i);
            modifiers.add(BloonModifier.valueOf(tag.asText()));
        }

        Texture image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%s/%s.png", bloonType, bloonVariant));
        image.setScaleX(0.5);
        image.setScaleY(0.5);
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(image);
    }

    public void pop() {
        FXGL.play("pop.wav");
        MainApp.addMoney(1);
        if (health > 1) {
            health--;
            return;
        }

        MainApp.gameXP++;

        if (properties.get("child").size() == 0) {
            remove();
            return;
        }
        if (properties.get("child").size() > 1) {
            for (int i = 1; i < properties.get("child").size(); i++) {
                Entity bloon = FXGL.getGameWorld().spawn("bloon");
                bloon.setPosition(entity.getPosition().add(new Point2D(-10 * i, 0)));
                bloon.getComponent(BloonsComponent.class).updateProperties(properties.get("child").get(i).textValue());
                MainApp.bloonList.add(bloon);
            }
        }

        updateProperties(properties.get("child").get(0).textValue());
    }
    public void remove() {
        if (regrowLoop != null) regrowLoop.expire();
        FXGL.getGameWorld().removeEntity(entity);
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    public Point2D getVelocity() {
        return this.velocity;
    }
    public int getHealth() {
        return this.health;
    }
    public int getRBE() {
        return this.RBE;
    }
    public ArrayList<BloonModifier> getModifiers() {
        return this.modifiers;
    }
}
