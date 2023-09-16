package com.os.apps.systemFileApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

public class SystemFileApp extends BaseApp {

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
}
