package org.btdj.game.Components.Towers.Interfaces;

import com.almasb.fxgl.entity.Entity;

import java.util.List;

/**
 * Interface for towers relying on radius detection
 *
 * @author Kasper Pajak
 * @version 1.0
 */
public interface RadiusTower {
    /**
     * Called upon firing
     * @param entities entities within radius
     */
    void invoke(List<Entity> entities);
}
