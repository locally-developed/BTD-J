package org.btdj.game.Logic;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import org.btdj.game.Components.WaypointComponent;

/**
 * Class containing all the bloon waypoint positions, and the code to initialize the waypoint functionality.
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class WaypointHandler {
    public WaypointHandler() {
        FXGL.entityBuilder()    //Creates a new waypoint
                .at(845, 400)   //Waypoint position
                .buildAndAttach()   //Adds the waypoint to the game scene
                .addComponent(new WaypointComponent(new Point2D(0, -1)));
                //Initializes the waypoint handler with a new velocity to follow
        FXGL.entityBuilder()
                .at(845, 135)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(-1, 0)));
        FXGL.entityBuilder()
                .at(490, 135)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, 1)));
        FXGL.entityBuilder()
                .at(490, 925)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(-1, 0)));
        FXGL.entityBuilder()
                .at(200, 925)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, -1)));
        FXGL.entityBuilder()
                .at(200, 555)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(1, 0)));
        FXGL.entityBuilder()
                .at(1075, 555)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, -1)));
        FXGL.entityBuilder()
                .at(1075, 300)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(1, 0)));
        FXGL.entityBuilder()
                .at(1260, 300)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, 1)));
        FXGL.entityBuilder()
                .at(1260, 830)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(-1, 0)));
        FXGL.entityBuilder()
                .at(680, 830)
                .buildAndAttach()
                .addComponent(new WaypointComponent(new Point2D(0, 1)));
    }
}
