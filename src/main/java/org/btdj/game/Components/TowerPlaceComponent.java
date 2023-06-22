package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.btdj.game.Components.Towers.*;
import org.btdj.game.MainApp;

/**
 * Handles the bloon waypoint functionality
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class TowerPlaceComponent extends Component {
    private boolean placed = false; //Debounce
    private boolean canPlace = false;
    private final String tower; //Tower name

    /**
     * Creates a new TowerPlaceComponent for a specified tower
     *
     * @param tower tower name
     */
    public TowerPlaceComponent(String tower) {
        this.tower = tower;
    }
    @Override
    public void onUpdate(double tpf) {  //time per frame
        if (!this.placed) { //Check if we haven't placed the tower yet
            Point2D mousePos = FXGL.getInput().getMousePositionWorld(); //Get the mouse position

            //A black and white mask of the level is used to determine whether a tower can be placed at a
            //specific point. The brightness is retrieved from the pixel at the mouse's location, and the validity of
            //the location for placement is determined depending on whether the pixel brightness is white or black.
            double colorAtPos = FXGL.getAssetLoader().loadImage("ui/level_mask.png")
                    .getPixelReader()
                    .getColor((int) mousePos.getX(), (int) mousePos.getY()).getBrightness();    //Brightness at position

            entity.getViewComponent().getChild(0, Rectangle.class).setFill(
                    colorAtPos == 0.0 ? Color.BLACK : Color.RED //Sets the tower-placer entity to red if unable to place
            );
            this.canPlace = colorAtPos == 0.0;

            entity.setPosition(mousePos.subtract(new Point2D(25,25)));  //Tracks the tower-placer to the mouse
        }
    }

    /**
     * Places the tower at the current mouse position
     */
    public void place() {
        if (!canPlace) return;  //Check if we can place the tower at the current mouse location

        this.placed = true; //Set debounce
        Point2D position = FXGL.getInput().getMousePositionWorld(); //Get the final mouse location
        Entity tower = FXGL.getGameWorld().spawn(this.tower);   //Spawns the specified tower using the entity factory
        tower.setPosition(position);
        Rectangle2D rangeCollider = new Rectangle2D(position.getX()-100, position.getY()-100, 200, 200);
        //Creates the rectangle which will act as the range collider, centered about the tower and with a radius of 200

        switch (this.tower) {   //This was the easiest way to call the setRangeCollider method across all the different
                                //tower components (I tried using inheritance, didn't work)
            case "dartMonkey" -> tower.getComponent(DartMonkeyComponent.class).setRangeCollider(rangeCollider);
            case "tackShooter" -> tower.getComponent(TackShooterComponent.class).setRangeCollider(rangeCollider);
            case "bombShooter" -> tower.getComponent(BombShooterComponent.class).setRangeCollider(rangeCollider);
            case "glueGunner" -> tower.getComponent(GlueGunnerComponent.class).setRangeCollider(rangeCollider);
            case "dartlingGunner" -> tower.getComponent(DartlingGunnerComponent.class).setRangeCollider(rangeCollider);
            case "iceMonkey" -> tower.getComponent(IceMonkeyComponent.class).setRangeCollider(rangeCollider);
        }

        MainApp.removePlacer(); //Removes the tower-placer once we're done
    }
}
