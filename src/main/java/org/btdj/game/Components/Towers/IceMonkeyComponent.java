package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import org.btdj.game.Components.BloonModifier;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.MainApp;


public class IceMonkeyComponent extends TowerComponent{
    private double coolDown = 2.6;
    private int processDelay = 0;
    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 2.6) {
            super.radiiTower((entities) -> {
                for (Entity bloon: entities) {
                    BloonsComponent bloonComponent = bloon.getComponent(BloonsComponent.class);

                    bloonComponent.frozenSpeedMod = 0;

                    Texture image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%S/ice.png", bloonComponent.bloonType));
                    image.setScaleX(0.5);
                    image.setScaleY(0.5);
                    image.setX(-5);
                    image.setY(-5);
                    bloon.getViewComponent().addChild(image);

                    FXGL.getGameTimer().runOnceAfter(() -> {
                        if (processDelay > 0 || MainApp.globalSpeedModifier > 1) {
                            bloonComponent.frozenSpeedMod = 1;
                            if (bloon.isActive()) bloon.getViewComponent().removeChild(image);
                            processDelay = 0;
                        } else {
                            processDelay++;
                        }
                    }, Duration.seconds(1.5));
                }
                coolDown = 0;
            }, e -> !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.FREEZING_IMMUNE) &&
                    e.getComponent(BloonsComponent.class).frozenSpeedMod == 1);
        }

        coolDown += tpf * MainApp.globalSpeedModifier;
    }
    public void setRangeCollider(Rectangle2D collider) {
        this.rangeCollider = collider;
    }
}
