package org.btdj.game.UI;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ButtonCreator {
    /**
     * Creates a tower purchase button
     *
     * @param position top-left corner of button
     * @param imagePath local path to tower image
     * @param backgroundHue HSV hue for button background (-1.0 < x < 1.0)
     * @param money cost of the tower
     * @return button group
     */
    public static Group create(Point2D position, String imagePath, double backgroundHue, int money) {
        ImageView background = new ImageView(FXGL.getAssetLoader().loadImage("ui/tower_bg.png"));
        background.setX(position.getX());
        background.setY(position.getY());
        background.setCache(true);  //You'll see this everywhere. Just means that the Node won't re-render every frame.

        //The button background image is in grayscale so that a ColorAdjust can be applied to tint it a certain color.
        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(backgroundHue);
        adjust.setSaturation(1.0);  //100% color
        background.setEffect(adjust);   //Applies the ColorAdjust to the BG picture

        Image image = FXGL.getAssetLoader().loadImage(imagePath);   //Getting tower image
        ImageView towerPortrait = new ImageView(image);
        towerPortrait.setX((100-image.getWidth())/2 + position.getX()); //Centering the image horizontally in the button
        towerPortrait.setY((125-image.getHeight())/2 + position.getY()); //Centering the image vertically
        towerPortrait.setCache(true);

        Text moneyText = TextCreator.create(String.format("$%d", money), 24);   //Creates the tower cost label
        moneyText.setX(position.getX()+25);
        moneyText.setY(position.getY()+120);

        Group buttonGroup = new Group(background, towerPortrait, moneyText);    //Bundling all the button elements
        hoverEffect(buttonGroup, background, adjust);   //Setting up the hover effect for the button
        return buttonGroup;
    }

    /**
     * Applies a darkening effect to a JavaFX Node when hovered over
     *
     * @param button root node of the button
     * @param adjust ColorAdjust object
     */
    public static void hoverEffect(Node button, ColorAdjust adjust) {
        button.setOnMouseEntered(e -> {
            adjust.setBrightness(-0.2);
            button.setEffect(adjust);
        });
        button.setOnMouseExited(e -> {
            adjust.setBrightness(0);
            button.setEffect(adjust);
        });
    }

    /**
     * Applies a darkening effect to a specific JavaFX Node when hovering over a specific Node
     *
     * @param buttonGroup root node of the button
     * @param button node which will be darkened
     * @param adjust ColorAdjust object
     */
    public static void hoverEffect(Node buttonGroup, Node button, ColorAdjust adjust) {
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
