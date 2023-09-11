package com.os.apps.systemFileApp;

import java.io.IOException;
import java.net.URL;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SystemFileApp extends BaseApp {

   public static void main(String[] args) {
      Application.launch(args);
   }

   public SystemFileApp() {
      super();
      super.fxmlPath = "/com/os/apps/systemFileApp/SystemFileApp.fxml";
      super.IconPath = "/com/os/img/task.png";
      super.TitleName = "可执行文件表";
   }
}
