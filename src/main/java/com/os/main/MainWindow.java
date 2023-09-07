package com.os.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow extends Application {
   public void start(Stage primaryStage) throws IOException, URISyntaxException {
      URL location = this.getClass().getResource("/com/os/main/MainWindow.fxml");
      if (location == null) {
         System.out.println("null");
      } else {
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(location);
         Parent root = (Parent)fxmlLoader.load();
         primaryStage.setTitle("os");
         Scene MainScene = new Scene(root);
         primaryStage.setScene(MainScene);
         Scene scene = primaryStage.getScene();
         final MainController mainController = fxmlLoader.getController();
         primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(mainController::adaptWindow));
         primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(mainController::adaptWindow));
         primaryStage.setMaximized(true);
         location = this.getClass().getResource("/com/os/img/Windows.png");
         primaryStage.getIcons().add(new Image(String.valueOf(location)));
         primaryStage.initStyle(StageStyle.UNDECORATED);
         primaryStage.show();
         mainController.init(scene, primaryStage);
         mainController.adaptWindow();
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
