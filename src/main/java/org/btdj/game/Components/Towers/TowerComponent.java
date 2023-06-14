package org.btdj.game.Components.Towers;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Rectangle2D;

public class TowerComponent extends Component {
    protected Rectangle2D rangeCollider;
    public void setRangeCollider(Rectangle2D collider) {
        this.rangeCollider = collider;
    }
}
