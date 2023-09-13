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
      // 获取FXML文件的URL
      URL location = this.getClass().getResource("/com/os/main/MainWindow.fxml");
      if (location == null) return;

      // 创建FXML加载器
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(location);

      // 加载FXML文件并创建根节点
      Parent root = fxmlLoader.load();

      // 创建主场景并将根节点添加到场景中
      Scene MainScene = new Scene(root);
      primaryStage.setScene(MainScene);

      // 获取当前场景
      Scene scene = primaryStage.getScene();

      // 获取主控制器
      final MainController mainController = fxmlLoader.getController();

      // 监听主窗口的宽度和高度变化，调整窗口
      primaryStage.widthProperty().addListener((observable, oldValue, newValue)
              -> Platform.runLater(mainController::adaptWindow));
      primaryStage.heightProperty().addListener((observable, oldValue, newValue)
              -> Platform.runLater(mainController::adaptWindow));

      // 最大化主窗口
      primaryStage.setMaximized(true);

      // 设置主窗口图标
      location = this.getClass().getResource("/com/os/img/Windows.png");
      primaryStage.getIcons().add(new Image(String.valueOf(location)));

      // 设置主窗口无边框
      primaryStage.initStyle(StageStyle.UNDECORATED);

      // 显示主窗口
      primaryStage.show();

      // 初始化主控制器，并调整窗口
      mainController.init(scene, primaryStage);
      mainController.adaptWindow();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
