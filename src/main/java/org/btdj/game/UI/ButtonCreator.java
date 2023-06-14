package org.btdj.game.UI;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.btdj.game.Components.ButtonComponent;

public class ButtonCreator {
    public static Group create(Point2D position, String imagePath, double backgroundHue) {
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

        Group buttonGroup = new Group(background, towerPortrait);
        new ButtonComponent(buttonGroup, background, adjust);
        return buttonGroup;
    }
}
