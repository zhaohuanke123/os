package com.os.applications.processApp;

import com.os.applications.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ProcessApp extends BaseApp<ProcessAppController> {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ProcessApp()
    {
        super("/com/os/applications/processApp/ProcessApp.fxml",
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
