package org.btdj.game.Components;

import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class BloonsComponent extends Component {
    public enum Type {
        RED,
        BLUE,
        GREEN,
        YELLOW,
        PINK
    };
    @Override
    public void onUpdate(double tpf) {

    }

    public void pop() {
        System.out.println(Type.RED.ordinal());
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("bloons/red.png"));
        ObjectMapper mapper = new ObjectMapper();
    }
}