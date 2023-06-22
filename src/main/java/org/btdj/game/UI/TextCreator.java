package org.btdj.game.UI;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

public class TextCreator {
    /**
     * Creates a new Text object with BTD-styled font, color, and outline
     * Font size 52
     *
     * @param placeholder placeholder text
     * @return stylized Text object
     */
    public static Text create(String placeholder) {
        Text text = new Text();
        text.setText(placeholder);
        //Loads font using game engine asset handler
        text.setFont(FXGL.getAssetLoader().loadFont("LuckiestGuy-Regular.ttf").newFont(52));
        stylize(text);
        return text;
    }
    /**
     * Creates a new Text object with BTD-styled font, color, and outline with a specific font size
     *
     * @param placeholder placeholder text
     * @param fontSize font size
     * @return stylized Text object
     */
    public static Text create(String placeholder, int fontSize) {
        Text text = new Text();
        text.setText(placeholder);
        text.setFont(FXGL.getAssetLoader().loadFont("LuckiestGuy-Regular.ttf").newFont(fontSize));
        stylize(text);
        return text;
    }
    private static void stylize(Text text) {
        text.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(2);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeLineJoin(StrokeLineJoin.ROUND);   //Rounded corners on text outline

        text.setCache(true);    //Reduces render lag by not having to re-render the text every singe frame,
                                //especially since we're computing the outer outline
    }
}
