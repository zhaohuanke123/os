package com.os.applications.resourcesOccupancyApp;

import com.os.applications.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ResourcesOccupancyApp extends BaseApp<ResourcesOccupancyAppController> {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ResourcesOccupancyApp() {
        super("/com/os/applications/resourcesOccupancyApp/ResourcesOccupancyApp.fxml",
                "/com/os/img/device.png",
                "占用管理器",
                1300.0,
                600.0);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        // 当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
