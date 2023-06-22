package org.btdj.game.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Rectangle2D;
import org.btdj.game.EntityType;
import org.btdj.game.MainApp;

import java.util.List;

/**
 * Handles the bloons which make it to the end of the track
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class EndComponent extends Component {
    @Override
    public void onUpdate(double tpf) {  //time per frame
        List<Entity> entitiesNearby = FXGL.getGameWorld().getEntitiesInRange(  //Gets any entities touching the end
                new Rectangle2D(entity.getX()-25, entity.getY()-25, 50, 50)
        );
        entitiesNearby = entitiesNearby.stream().filter(e -> e.getType() == EntityType.BLOON).toList();
        //Filters out any entities that are not bloons

        for (Entity bloon: entitiesNearby) {
            //Removes health depending on the bloon's RBE (Red Bloon Equivalent)
            MainApp.removeHealth(bloon.getComponent(BloonsComponent.class).getRBE());
            bloon.getComponent(BloonsComponent.class).remove(); //Removing the bloon at the end
        }
    }
}
