package com.os.utils.ui;

import javafx.scene.layout.Region;

public class CompSet {
    public static void SetCompSize(Region region, double width, double height) {
        if (region == null)
            return;
        if (width >= 0) {
            region.setMaxWidth(width);
            region.setMinWidth(width);
            region.setPrefWidth(width);
        }
        if (height >= 0) {
            region.setMaxHeight(height);
            region.setMinHeight(height);
            region.setPrefHeight(height);
        }
    }

}
