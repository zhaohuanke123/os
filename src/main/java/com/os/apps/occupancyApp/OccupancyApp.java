package com.os.apps.occupancyApp;

import java.io.IOException;
import java.net.URL;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OccupancyApp extends BaseApp {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public OccupancyApp() {
        super();
        super.fxmlPath = "/com/os/apps/occupancyApp/OccupancyApp.fxml";
        super.IconPath = "/com/os/img/device.png";
        super.TitleName = "占用管理器";
    }
}
