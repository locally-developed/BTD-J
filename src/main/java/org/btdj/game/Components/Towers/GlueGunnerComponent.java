package org.btdj.game.Components.Towers;

import org.btdj.game.Components.BloonsComponent;

public class GlueGunnerComponent extends TowerComponent{
    private double coolDown = 1;

    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1) {
            super.trackTower((target) -> {
                target.getComponent(BloonsComponent.class).setSpeedMultiplier(0.5);
                coolDown = 0;
            });
        }
        coolDown += tpf;
    }
}
