package com.os.apps.helpApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

public class HelpApp extends BaseApp {
    public static void main(String[] args) {
        Application.launch(args);
    }

    public HelpApp() {
        super();
        super.fxmlPath = "/com/os/apps/helpApp/HelpApp.fxml";
        super.IconPath = "/com/os/img/help.png";
        super.TitleName = "帮助";
    }
}
