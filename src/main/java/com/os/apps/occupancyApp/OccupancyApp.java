package com.os.apps.occupancyApp;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class OccupancyApp extends BaseApp<OccupancyAppController> {

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

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        // 当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
