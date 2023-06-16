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
    public static Group create(Point2D position, String imagePath, double backgroundHue, int money) {
        ImageView background = new ImageView(FXGL.getAssetLoader().loadImage("ui/tower_bg.png"));
        background.setX(position.getX());
        background.setY(position.getY());

        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(backgroundHue);
        adjust.setSaturation(1.0);
        background.setEffect(adjust);

        Image image = FXGL.getAssetLoader().loadImage(imagePath);
        ImageView towerPortrait = new ImageView(image);
        towerPortrait.setX((100-image.getWidth())/2 + position.getX());
        towerPortrait.setY((125-image.getHeight())/2 + position.getY());

        Text moneyText = TextCreator.create(String.format("$%d", money), 24);
        moneyText.setX(position.getX()+25);
        moneyText.setY(position.getY()+120);

        Group buttonGroup = new Group(background, towerPortrait, moneyText);
        hoverEffect(buttonGroup, background, adjust);
        return buttonGroup;
    }

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
