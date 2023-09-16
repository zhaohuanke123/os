package com.os.utils.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

public class CompSet {
    public static void setImageViewSize(ImageView button, double width, double height) {
        button.setFitWidth(width);
        button.setFitHeight(height);
    }

    public static void setCompSize(Region region, double width, double height) {
        setCompWidth(region, width);
        setCompHeight(region, height);
    }

    public static void setCompWidth(Region region, double width) {
        if (region == null)
            return;
        if (width >= 0) {
            region.setMaxWidth(width);
            region.setMinWidth(width);
            region.setPrefWidth(width);
        }
    }

    public static void setCompHeight(Region region, double height) {
        if (region == null)
            return;
        if (height >= 0) {
            region.setMaxHeight(height);
            region.setMinHeight(height);
            region.setPrefHeight(height);
        }
    }

}
