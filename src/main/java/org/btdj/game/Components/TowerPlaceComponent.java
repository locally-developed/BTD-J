package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.btdj.game.Components.Towers.*;
import org.btdj.game.MainApp;

public class TowerPlaceComponent extends Component {
    private boolean placed = false;
    private boolean canPlace = false;
    private final String tower;
    public TowerPlaceComponent(String tower) {
        this.tower = tower;
    }
    @Override
    public void onUpdate(double tpf) {
        if (!this.placed) {
            Point2D mousePos = FXGL.getInput().getMousePositionWorld();
            double colorAtPos = FXGL.getAssetLoader().loadImage("ui/level_mask.png")
                    .getPixelReader()
                    .getColor((int) mousePos.getX(), (int) mousePos.getY()).getBrightness();

            entity.getViewComponent().getChild(0, Rectangle.class).setFill(
                    colorAtPos == 0.0 ? Color.RED : Color.BLACK
            );
            this.canPlace = colorAtPos == 0.0;

            entity.setPosition(mousePos.subtract(new Point2D(25,25)));
        }
    }

    public void place() {
        if (!canPlace) return;

        this.placed = true;
        Point2D position = FXGL.getInput().getMousePositionWorld();
        Entity tower = FXGL.getGameWorld().spawn(this.tower);
        tower.setPosition(position);
        Rectangle2D rangeCollider = new Rectangle2D(position.getX()-100, position.getY()-100, 200, 200);

        switch (this.tower) {
            case "dartMonkey" -> tower.getComponent(DartMonkeyComponent.class).setRangeCollider(rangeCollider);
            case "tackShooter" -> tower.getComponent(TackShooterComponent.class).setRangeCollider(rangeCollider);
            case "bombShooter" -> tower.getComponent(BombShooterComponent.class).setRangeCollider(rangeCollider);
            case "glueGunner" -> tower.getComponent(GlueGunnerComponent.class).setRangeCollider(rangeCollider);
            case "dartlingGunner" -> tower.getComponent(DartlingGunnerComponent.class).setRangeCollider(rangeCollider);
        }

        Circle collider = new Circle(position.getX()+12.5, position.getY()+12.5, 150);
        collider.setOpacity(0.2);
        FXGL.getGameScene().addChild(collider);

        MainApp.removePlacer();
    }
}
