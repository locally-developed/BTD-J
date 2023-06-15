package org.btdj.game.Components.Towers;

import org.btdj.game.Components.BloonsComponent;

public class DartMonkeyComponent extends TowerComponent{
    private double coolDown = 1;

    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1) {
            super.trackTower((target) -> {
                target.getComponent(BloonsComponent.class).pop();
                coolDown = 0;
            });
        }
        coolDown += tpf;
    }
}
