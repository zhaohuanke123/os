package com.os.apps.fileApp.app;

import com.os.apps.fileApp.Controller.TipWindowCtl;
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

public class TipWindow extends Application {
   String tipString = "";

   public static void main(String[] args) {
      launch(args);
   }

   public TipWindow(String tipString) {
      this.tipString = tipString;
   }

   public void start(Stage primaryStage) throws IOException {
      URL location = this.getClass().getResource("/com/os/apps/fileApp/fxmls/TipWindow.fxml");
      if (location == null) {
         System.out.println("null");
      } else {
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(location);
         Parent root = (Parent)fxmlLoader.load();
         primaryStage.setTitle("提示");
         Scene MainScene = new Scene(root);
         primaryStage.setScene(MainScene);
         Scene scene = primaryStage.getScene();
         TipWindowCtl tipWindowCtl = (TipWindowCtl)fxmlLoader.getController();
         location = this.getClass().getResource("/com/os/apps/fileApp/res/tip.png");
         primaryStage.getIcons().add(new Image(String.valueOf(location)));
         scene.setFill(Color.TRANSPARENT);
         primaryStage.initStyle(StageStyle.TRANSPARENT);
         primaryStage.show();
         tipWindowCtl.init(primaryStage, this.tipString);
         primaryStage.setResizable(false);
      }
   }
}
