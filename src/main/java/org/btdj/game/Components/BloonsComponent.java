package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Point2D;


public class BloonsComponent extends Component {

    public enum Type {
        RED,
        BLUE,
        GREEN,
        YELLOW,
        PINK
    };

    private Point2D velocity = new Point2D(0,0);

    @Override
    public void onUpdate(double tpf) {
        entity.translate(velocity);
    }


    public void pop() {
        System.out.println(Type.RED.ordinal());
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("bloons/red.png"));
        ObjectMapper mapper = new ObjectMapper();
    }
    public void remove(int a) {
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
