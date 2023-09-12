package com.os.apps;

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
    protected String fxmlPath;  // FXML 文件的路径
    protected String IconPath;  // 图标路径
    protected String TitleName;  // 标题名称

    @Override
    public void start(Stage stage) throws IOException {
        // 获取FXML文件的URL
        URL location = this.getClass().getResource(fxmlPath);
        if (location == null) return;

        // 创建FXML加载器
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);

        // 加载FXML文件并创建根节点
        Parent root = fxmlLoader.load();

        // 设置窗口标题
        stage.setTitle(TitleName);

        // 创建场景并将根节点添加到场景中
        Scene MainScene = new Scene(root);
        stage.setScene(MainScene);

        // 获取当前场景
        Scene scene = stage.getScene();

        // 获取控制器
        BaseController appController = fxmlLoader.getController();

        // 设置窗口图标
        location = this.getClass().getResource(IconPath);
        stage.getIcons().add(new Image(String.valueOf(location)));

        // 禁止窗口调整大小
        stage.setResizable(false);
        // 设置场景背景为透明
        scene.setFill(Color.TRANSPARENT);
        // 设置窗口样式为透明
        stage.initStyle(StageStyle.TRANSPARENT);

        // 显示窗口
        stage.show();

        // 当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));

        // 初始化控制器
        appController.init(stage);
        // 调整控制器窗口
        appController.adaptWindow();
    }
}
