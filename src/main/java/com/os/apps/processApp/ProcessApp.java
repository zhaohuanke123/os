package com.os.apps.processApp;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

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
