package com.os.apps.processApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

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
}
