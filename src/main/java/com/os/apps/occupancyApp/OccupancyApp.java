package com.os.apps.occupancyApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

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
