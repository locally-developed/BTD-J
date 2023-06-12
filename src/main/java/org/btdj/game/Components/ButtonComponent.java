package org.btdj.game.Components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ButtonComponent {
    private final Rectangle button;
    public ButtonComponent(Rectangle button, Color color, Color hoverColor) {
        this.button = button;

        button.setFill(color);
        button.setOnMouseEntered(e -> button.setFill(hoverColor));
        button.setOnMouseExited(e -> button.setFill(color));
    }
}
