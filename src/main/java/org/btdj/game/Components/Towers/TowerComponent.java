package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.btdj.game.Components.BloonModifier;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.Components.Towers.Interfaces.RadiusTower;
import org.btdj.game.Components.Towers.Interfaces.TowerFilter;
import org.btdj.game.Components.Towers.Interfaces.TrackTower;
import org.btdj.game.EntityType;

import java.util.Comparator;
import java.util.List;

/**
 * Base component for all towers, handles rudimentary tracking and bloon filtering
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public class TowerComponent extends Component {
    private final GameWorld world = FXGL.getGameWorld();
    protected TargetingPriority targetingPriority = TargetingPriority.FIRST;    //Default tracking priority
    protected Rectangle2D rangeCollider;
    public void setRangeCollider(Rectangle2D collider) {
        this.rangeCollider = collider;
    }

    /**
     * Targets bloons within a radius and selects one to fire at
     *
     * @param trackTower method invoked when finished targeting
     */
    public void trackTower(TrackTower trackTower) {
        List<Entity> bloonsInRange = world.getEntitiesInRange(this.rangeCollider);  //Gets bloons within the radius

        switch (targetingPriority) {    //Different sorting depending on targeting priority
            case FIRST: //Targets the first bloon
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == EntityType.BLOON &&
                                !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.CAMO))
                                //Ignores camo bloons (invisible to towers)
                        .toList();
                break;
            case STRONGEST: //Targets the strongest bloon within its range
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == EntityType.BLOON &&
                                !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.CAMO))
                        .sorted(Comparator.comparingInt(bloon ->
                                bloon.getComponent(BloonsComponent.class).getRBE())
                                //Determines the strongest bloon based off of RBE
                        )
                        .toList();
                break;
        }

        if (!bloonsInRange.isEmpty()) {
            aim(trackTower, bloonsInRange); //If a bloon was successfully targeted, aim the tower at said bloon
        }
    }
    /**
     * Targets bloons within a radius and selects one to fire at using a custom bloon filter
     *
     * @param trackTower method invoked when finished targeting
     * @param towerFilter custom bloon filter
     */
    public void trackTower(TrackTower trackTower, TowerFilter towerFilter) {
        List<Entity> bloonsInRange = world.getEntitiesInRange(this.rangeCollider);

        switch (targetingPriority) {
            case FIRST:
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == EntityType.BLOON && towerFilter.test(e) &&
                                !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.CAMO))
                        //Checks against the custom filter
                        .toList();
                break;
            case STRONGEST:
                bloonsInRange = bloonsInRange
                        .stream()
                        .filter(e -> e.getType() == EntityType.BLOON && towerFilter.test(e) &&
                                !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.CAMO))
                        .sorted(Comparator.comparingInt(bloon ->
                                bloon.getComponent(BloonsComponent.class).getRBE())
                        )
                        .toList();
                break;
        }

        if (!bloonsInRange.isEmpty()) {
            aim(trackTower, bloonsInRange);
        }
    }

    /**
     * Gets all bloons within a certain radius around the tower
     *
     * @param radiusTower method invoked when finished retrieving bloons within radius
     */
    public void radiiTower(RadiusTower radiusTower) {
        List<Entity> entities = FXGL.getGameWorld().getEntitiesInRange(rangeCollider)   //Gets towers within the radius
                .stream()
                .filter(e -> e.getType() == EntityType.BLOON &&
                        !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.CAMO))
                //Filter out the camo bloons
                .toList();
        if (entities.isEmpty()) return;
        radiusTower.invoke(entities);   //Invoke the method passed
    }
    public void radiiTower(RadiusTower radiusTower, TowerFilter towerFilter) {
        List<Entity> entities = FXGL.getGameWorld().getEntitiesInRange(rangeCollider)   //Gets towers within the radius
                .stream()
                .filter(e -> e.getType() == EntityType.BLOON && towerFilter.test(e) &&
                        !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.CAMO))
                //Checks against the custom filter, ignores camo
                .toList();
        if (entities.isEmpty()) return;
        radiusTower.invoke(entities);   //Invoke the method passed
    }

    /**
     * Aims the tower in the direction of a bloon.
     *
     * @param trackTower method invoked upon firing
     * @param bloonsInRange list of bloons within range
     */
    private void aim(TrackTower trackTower, List<Entity> bloonsInRange) {
        Entity target = null;
        switch (targetingPriority) {
            case FIRST, WEAKEST -> target = bloonsInRange.get(0);   //Gets the first bloon in the list (first, weakest)
            case LAST, STRONGEST -> target = bloonsInRange.get(bloonsInRange.size() - 1);   //Gets the last bloon
        }

        Point2D p1 = target.getPosition().subtract(entity.getPosition());   //Magnitude between tower and bloon position
        Point2D lookVector = new Point2D(1, 0); //Location of the front of the tower (right side)
        double angle = lookVector.angle(p1);    //Angle in radians at which the bloon is located from the tower

        entity.setRotation(angle);  //Points the tower at the bloon

        trackTower.invoke(target);   //Invoke the method passed
    }
}
