package com.os.utils.uiUtil;

import com.os.main.MainController;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CompSet {
    public static void setStageSize(Stage stage, double width, double height) {
        setStageWidth(stage, width);
        setStageHeight(stage, height);
    }

    private static void setStageHeight(Stage stage, double height) {
        stage.setMaxHeight(MainController.getInstance().sceneHeight);
        stage.setMinHeight(height);
        stage.setHeight(height);
    }

    private static void setStageWidth(Stage stage, double width) {
        stage.setMaxWidth(MainController.getInstance().sceneWidth);
        stage.setMinWidth(width);
        stage.setWidth(width);
    }

    public static void setImageViewFixSize(ImageView button, double width, double height) {
        button.setFitWidth(width);
        button.setFitHeight(height);
    }

    public static void setCompFixSize(Region region, double width, double height) {
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
