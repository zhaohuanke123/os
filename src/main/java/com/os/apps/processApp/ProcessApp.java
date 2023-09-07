package com.os.apps.processApp;

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

public class ProcessApp extends Application {
   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage primaryStage) throws IOException {
      URL location = this.getClass().getResource("/com/os/apps/processApp/ProcessApp.fxml");
      if (location == null) {
         System.out.println("null");
      } else {
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(location);
         Parent root = fxmlLoader.load();
         primaryStage.setTitle("进程管理器");
         Scene MainScene = new Scene(root);
         primaryStage.setScene(MainScene);
         Scene scene = primaryStage.getScene();
         ProcessAppController processAppController = fxmlLoader.getController();
         location = this.getClass().getResource("/com/os/img/process.png");
         primaryStage.getIcons().add(new Image(String.valueOf(location)));
         primaryStage.setResizable(false);
         scene.setFill(Color.TRANSPARENT);
         primaryStage.initStyle(StageStyle.TRANSPARENT);
         primaryStage.show();
         processAppController.init(primaryStage);
         primaryStage.setOnCloseRequest(event -> System.exit(0));
         processAppController.adaptWindow();
      }
   }
}
