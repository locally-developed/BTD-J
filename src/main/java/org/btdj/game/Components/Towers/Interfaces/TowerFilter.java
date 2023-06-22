package org.btdj.game.Components.Towers.Interfaces;

import com.almasb.fxgl.entity.Entity;

/**
 * Interface used to handle custom bloon filtering
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public interface TowerFilter {
    /**
     * Called when testing a bloon entity
     * @param entity entity to test
     */
    boolean test(Entity entity);
}
