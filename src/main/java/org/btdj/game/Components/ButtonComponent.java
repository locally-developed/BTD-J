package org.btdj.game.Components;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;

public class ButtonComponent {
    private final Node button;

    public ButtonComponent(Node button, ColorAdjust adjust) {
        this.button = button;

        button.setOnMouseEntered(e -> {
            adjust.setBrightness(-0.2);
            button.setEffect(adjust);
        });
        button.setOnMouseExited(e -> {
            adjust.setBrightness(0);
            button.setEffect(adjust);
        });
    }

    public ButtonComponent(Node buttonGroup, Node button, ColorAdjust adjust) {
        this.button = button;

        buttonGroup.setOnMouseEntered(e -> {
            adjust.setBrightness(-0.2);
            button.setEffect(adjust);
        });
        buttonGroup.setOnMouseExited(e -> {
            adjust.setBrightness(0);
            button.setEffect(adjust);
        });
    }
}
