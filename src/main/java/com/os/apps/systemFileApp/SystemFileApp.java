package com.os.apps.systemFileApp;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SystemFileApp extends Application {
   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage primaryStage) throws IOException {
      URL location = this.getClass().getResource("/com/os/apps/systemFileApp/SystemFileApp.fxml");
      if (location == null) {
         System.out.println("null");
      } else {
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(location);
         Parent root = fxmlLoader.load();
         primaryStage.setTitle("可执行文件表");
         Scene MainScene = new Scene(root);
         primaryStage.setScene(MainScene);
         Scene scene = primaryStage.getScene();
         SystemFileAppController systemFileAppController = fxmlLoader.getController();
         location = this.getClass().getResource("/com/os/img/task.png");
         primaryStage.getIcons().add(new Image(String.valueOf(location)));
         scene.setFill(Color.TRANSPARENT);
         primaryStage.initStyle(StageStyle.TRANSPARENT);
         primaryStage.show();
         systemFileAppController.init(primaryStage);
         primaryStage.setResizable(false);
         systemFileAppController.adaptWindow();
      }
   }
}
