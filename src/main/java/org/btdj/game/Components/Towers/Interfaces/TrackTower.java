package org.btdj.game.Components.Towers.Interfaces;

import com.almasb.fxgl.entity.Entity;

/**
 * Interface for towers relying on tracking bloons within a radius
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public interface TrackTower {
    /**
     * Called upon firing
     * @param target tracked entity
     */
    void invoke(Entity target);
}
