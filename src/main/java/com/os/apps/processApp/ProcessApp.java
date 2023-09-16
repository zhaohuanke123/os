package com.os.apps.processApp;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ProcessApp extends BaseApp {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ProcessApp()
    {
        super("/com/os/apps/processApp/ProcessApp.fxml",
                "/com/os/img/process.png",
                "进程管理器",
                1100.0,
                500.0);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        // 当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
