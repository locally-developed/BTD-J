package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.util.Duration;
import org.btdj.game.Components.BloonModifier;
import org.btdj.game.Components.BloonsComponent;
import org.btdj.game.MainApp;

public class GlueGunnerComponent extends TowerComponent{
    private double coolDown = 1;
    private int processDelay = 0;

    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1) {
            super.trackTower(target -> {
                BloonsComponent bloon = target.getComponent(BloonsComponent.class);

                bloon.speedMultiplier *= 0.5;
                bloon.glued = true;

                Texture image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%S/%s.png",
                        bloon.bloonType,
                        bloon.getModifiers().contains(BloonModifier.REGROW) ?
                                "regrowGlue" : "glue"
                ));
                image.setScaleX(0.5);
                image.setScaleY(0.5);
                target.getViewComponent().addChild(image);

                FXGL.getGameTimer().runOnceAfter(() -> {
                    if (processDelay > 0 || MainApp.globalSpeedModifier > 1) {
                        bloon.speedMultiplier *= 2;
                        bloon.glued = false;
                        if (target.isActive()) target.getViewComponent().removeChild(image);
                        processDelay = 0;
                    } else {
                        processDelay++;
                    }
                }, Duration.seconds(0.75));
                coolDown = 0;
            }, e -> !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.GLUE_IMMUNE) &&
                    !e.getComponent(BloonsComponent.class).glued);
        }
        coolDown += tpf * MainApp.globalSpeedModifier;
    }
}
