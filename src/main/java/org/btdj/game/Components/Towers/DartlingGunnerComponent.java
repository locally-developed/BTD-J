package org.btdj.game.Components.Towers;

import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.MainApp;

public class DartlingGunnerComponent extends TowerComponent{
    private double coolDown = 0.2;

    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 0.2) {
            super.trackTower((target) -> {
                target.getComponent(BloonsComponent.class).pop();
                coolDown = 0;
            });
        }
        coolDown += tpf * MainApp.globalSpeedModifier;
    }
}
