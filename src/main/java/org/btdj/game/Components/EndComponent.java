package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Rectangle2D;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;

import java.util.List;

public class EndComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        List<Entity> entitiesNearby = FXGL.getGameWorld().getEntitiesInRange(
                new Rectangle2D(entity.getX()-25, entity.getY()-25, 50, 50)
        );
        entitiesNearby = entitiesNearby.stream().filter(e -> e.getType() == EntityType.BLOON).toList();

        for (Entity bloon: entitiesNearby) {
            MainApp.gameHealth -= bloon.getComponent(BloonsComponent.class).getRBE();
            bloon.getComponent(BloonsComponent.class).remove();
        }
    }
}
