package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.MouseTrigger;
import com.almasb.fxgl.input.TriggerListener;
import javafx.geometry.Point2D;
import org.btdj.game.MainApp;
import org.jetbrains.annotations.NotNull;

public class TowerPlaceComponent extends Component {
    private TriggerListener triggerListener = new TriggerListener() {
        @Override
        protected void onButtonBegin(@NotNull MouseTrigger mouseTrigger) {
            System.out.println("click");
            place(FXGL.getInput().getMousePositionWorld());
        }
    };
    public TowerPlaceComponent() {
        FXGL.getInput().addTriggerListener(triggerListener);
    }
    @Override
    public void onUpdate(double tpf) {
        entity.setPosition(FXGL.getInput().getMousePositionWorld());
    }

    private void place(Point2D position) {
        FXGL.getGameWorld().removeEntity(entity);
        FXGL.getInput().stopCapture();
    }
}
