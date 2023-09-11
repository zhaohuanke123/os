package com.os.apps.processApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

public class ProcessApp extends BaseApp {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ProcessApp() {
        super();

        super.fxmlPath = "/com/os/apps/processApp/ProcessApp.fxml";
        super.IconPath = "/com/os/img/process.png";
        super.TitleName = "进程管理器";
    }
}
