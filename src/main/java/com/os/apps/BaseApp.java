package com.os.apps;

import com.os.apps.helpApp.HelpAppController;
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

public class BaseApp extends Application {
    protected String fxmlPath;
    protected String IconPath;
    protected String TitleName;

    @Override
    public void start(Stage stage) throws IOException {
        URL location = this.getClass().getResource(fxmlPath);
        if (location == null) {
            System.out.println("null");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            Parent root = fxmlLoader.load();
            stage.setTitle(TitleName);
            Scene MainScene = new Scene(root);
            stage.setScene(MainScene);
            Scene scene = stage.getScene();
            BaseController appController = fxmlLoader.getController();
            location = this.getClass().getResource(IconPath);
            stage.getIcons().add(new Image(String.valueOf(location)));
            stage.setResizable(false);
            appController.init(stage);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            stage.setOnCloseRequest(event -> System.exit(0));
            appController.adaptWindow();
        }
    }
}
