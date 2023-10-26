package com.os.applications.processControlApp;

import com.os.applications.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ProcessControlApp extends BaseApp<ProcessControlAppController> {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ProcessControlApp()
    {
        super("/com/os/applications/processControlApp/ProcessControlApp.fxml",
                "/com/os/img/process.png",
                "进程管理器",
                1100.0,
                550.0);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        // 当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
