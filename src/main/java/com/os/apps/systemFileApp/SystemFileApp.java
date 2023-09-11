package com.os.apps.systemFileApp;

import com.os.apps.BaseApp;
import javafx.application.Application;

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
