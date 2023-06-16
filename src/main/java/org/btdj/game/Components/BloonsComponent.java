package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Point2D;
import org.btdj.game.Components.Towers.BloonModifier;
import org.btdj.game.MainApp;
import org.btdj.game.Util.JsonParser;

import java.io.IOException;
import java.util.ArrayList;


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
    private final ArrayList<BloonModifier> modifiers = new ArrayList<>();

    @Override
    public void onUpdate(double tpf) {
        entity.translate(this.velocity.multiply(2 * speed * speedMultiplier * MainApp.globalSpeedModifier));
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
        if (passedMods.contains(BloonModifier.REGROW)) bloonVariant = "regrow";
        if (passedMods.contains(BloonModifier.CAMO) && passedMods.contains(BloonModifier.REGROW)) bloonVariant = "camoRegrow";

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

        Texture image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%S/%S.png", bloonType, bloonVariant));
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
