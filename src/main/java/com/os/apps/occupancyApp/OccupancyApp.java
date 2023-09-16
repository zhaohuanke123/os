package com.os.apps.occupancyApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

public class OccupancyApp extends BaseApp {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public OccupancyApp() {
        super("/com/os/apps/occupancyApp/OccupancyApp.fxml",
                "/com/os/img/device.png",
                "占用管理器",
                1000.0,
                530.0);
    }

}
