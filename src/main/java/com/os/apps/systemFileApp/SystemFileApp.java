package com.os.apps.systemFileApp;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class SystemFileApp extends BaseApp<SystemFileAppController> {

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public SystemFileApp() {
        super("/com/os/apps/systemFileApp/SystemFileApp.fxml",
                "/com/os/img/task.png",
                "可执行文件表",
                500.0,
                500.0);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        // 当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
