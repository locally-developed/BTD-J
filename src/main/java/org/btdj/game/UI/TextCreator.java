package org.btdj.game.UI;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

public class TextCreator {
    public static Text create(String placeholder) {
        Text text = new Text();
        text.setText(placeholder);
        text.setFont(FXGL.getAssetLoader().loadFont("LuckiestGuy-Regular.ttf").newFont(52));
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(2);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeLineJoin(StrokeLineJoin.ROUND);
        return text;
    }
    public static Text create(String placeholder, int fontSize) {
        Text text = new Text();
        text.setText(placeholder);
        text.setFont(FXGL.getAssetLoader().loadFont("LuckiestGuy-Regular.ttf").newFont(fontSize));
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(2);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeLineJoin(StrokeLineJoin.ROUND);
        return text;
    }
}
