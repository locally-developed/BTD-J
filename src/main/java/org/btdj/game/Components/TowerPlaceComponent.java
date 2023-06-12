package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import org.btdj.game.MainApp;

public class TowerPlaceComponent extends Component {
    private boolean placed = false;
    public TowerPlaceComponent() {}
    @Override
    public void onUpdate(double tpf) {
        if (!this.placed) {
            entity.setPosition(FXGL.getInput().getMousePositionWorld());
        }
    }

    public void place() {
        this.placed = true;
        Point2D position = FXGL.getInput().getMousePositionWorld();
        Entity tower = FXGL.getGameWorld().spawn("tower");
        tower.setPosition(position);
        tower.getComponent(TowerComponent.class).setRangeCollider(
                new Rectangle2D(position.getX()-100, position.getY()-100, 200, 200)
        );

        Rectangle collider =  new Rectangle(position.getX()+12.5-100, position.getY()+12.5-100, 200, 200);
        collider.setOpacity(0.2);
        FXGL.getGameScene().addChild(collider);

        MainApp.removePlacer();
    }
}
