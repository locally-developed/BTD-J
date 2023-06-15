package org.btdj.game.Logic;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import org.btdj.game.Components.WaypointComponent;

public class WaypointHandler {
    public WaypointHandler() {
        FXGL.entityBuilder()
                .at(815, 450)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, -1)));
        FXGL.entityBuilder()
                .at(815, 150)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(-1, 0)));
        FXGL.entityBuilder()
                .at(500, 150)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, 1)));
        FXGL.entityBuilder()
                .at(500, 850)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(-1, 0)));
        FXGL.entityBuilder()
                .at(220, 850)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, -1)));
        FXGL.entityBuilder()
                .at(220, 580)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(1, 0)));
        FXGL.entityBuilder()
                .at(1025, 580)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, -1)));
        FXGL.entityBuilder()
                .at(1025, 325)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(1, 0)));
        FXGL.entityBuilder()
                .at(1245, 325)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, 1)));
        FXGL.entityBuilder()
                .at(1245, 795)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(-1, 0)));
        FXGL.entityBuilder()
                .at(695, 795)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, 1)));
    }
}
