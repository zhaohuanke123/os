package com.os.apps.helpApp;

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

public class HelpApp extends Application {
   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage primaryStage) throws IOException {
      URL location = this.getClass().getResource("/com/os/apps/helpApp/HelpApp.fxml");
      if (location == null) {
         System.out.println("null");
      } else {
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(location);
         Parent root = fxmlLoader.load();
         primaryStage.setTitle("帮助");
         Scene MainScene = new Scene(root);
         primaryStage.setScene(MainScene);
         Scene scene = primaryStage.getScene();
         HelpAppController helpAppController = fxmlLoader.getController();
         location = this.getClass().getResource("/com/os/img/help.png");
         primaryStage.getIcons().add(new Image(String.valueOf(location)));
         primaryStage.setResizable(false);
         helpAppController.init(primaryStage);
         scene.setFill(Color.TRANSPARENT);
         primaryStage.initStyle(StageStyle.TRANSPARENT);
         primaryStage.show();
         primaryStage.setOnCloseRequest(event -> System.exit(0));
         helpAppController.adaptWindow();
      }
   }
}
