package org.btdj.game.Components;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ButtonComponent {
    private final Node button;
    public ButtonComponent(Node button) {
        this.button = button;
        ColorAdjust adjust = new ColorAdjust();

        button.setOnMouseEntered(e -> {
            adjust.setBrightness(-0.2);
            button.setEffect(adjust);
        });
        button.setOnMouseExited(e -> {
            adjust.setBrightness(0);
            button.setEffect(adjust);
        });
    }
}
