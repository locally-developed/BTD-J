package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;


public class DartComponent extends Component {
    private double travelTime = 0;
    private Point2D velocity = new Point2D(0,0);

    @Override
    public void onUpdate(double tpf) {
        if (travelTime >= 1) FXGL.getGameWorld().removeEntity(entity);

        entity.translate(velocity);
        travelTime+=tpf;
    }
}
