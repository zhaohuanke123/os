package com.os.apps.helpApp;

import com.os.apps.BaseApp;
import com.sun.tools.javac.Main;
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
