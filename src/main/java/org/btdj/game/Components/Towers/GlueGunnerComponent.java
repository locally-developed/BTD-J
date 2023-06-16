package org.btdj.game.Components.Towers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.util.Duration;
import org.btdj.game.Components.BloonsComponent;

public class GlueGunnerComponent extends TowerComponent{
    private double coolDown = 1;

    @Override
    public void onUpdate(double tpf) {
        if (coolDown >= 1) {
            super.trackTower(target -> {
                BloonsComponent bloon = target.getComponent(BloonsComponent.class);

                bloon.speedMultiplier *= 0.5;
                bloon.glued = true;

                Texture image = FXGL.getAssetLoader().loadTexture(String.format("bloons/%S/glue.png", bloon.bloonType));
                image.setScaleX(0.5);
                image.setScaleY(0.5);
                target.getViewComponent().addChild(image);

                FXGL.getGameTimer().runOnceAfter(() -> {
                    bloon.speedMultiplier *= 2;
                    bloon.glued = false;
                    if (target.isActive()) target.getViewComponent().removeChild(image);
                }, Duration.seconds(1.5));
                coolDown = 0;
            }, e -> !e.getComponent(BloonsComponent.class).getModifiers().contains(BloonModifier.GLUE_IMMUNE) &&
                    !e.getComponent(BloonsComponent.class).glued);
        }
        coolDown += tpf;
    }
}
