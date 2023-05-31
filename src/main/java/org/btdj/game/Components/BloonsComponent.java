package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Point2D;
import org.btdj.game.MainApp;
import org.btdj.game.Util.JsonParser;

import java.io.IOException;


public class BloonsComponent extends Component {
    private Point2D velocity = new Point2D(0,0);
    private JsonNode properties;
    private String bloonType;

    @Override
    public void onUpdate(double tpf) {
        entity.translate(velocity);
    }

    public void updateProperties(String type) {
        try {
            properties = JsonParser.getBloonFromJson(type);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.bloonType = type;
        setVelocity(new Point2D(1.5 * properties.get("speed").asDouble(), 0));

        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture(String.format("bloons/%S.png", bloonType)));
    }

    public void pop() {
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

        FXGL.play("pop.wav");

        updateProperties(properties.get("child").get(0).textValue());
    }
    public void remove() {
        FXGL.getGameWorld().removeEntity(entity);
    }
    public void setVelocity() {
        this.velocity = new Point2D(1,2);
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return this.velocity;
    }
}
