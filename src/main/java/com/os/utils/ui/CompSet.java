package com.os.utils.ui;

import javafx.scene.layout.Region;

public class CompSet {
    public static void SetRegionSize(Region region, double width, double height) {
        region.setPrefSize(width, height);
        region.setMinSize(width, height);
        region.setMaxSize(width, height);
    }

}
