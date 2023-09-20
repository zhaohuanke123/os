package com.os.apps;

import com.os.main.MainController;
import com.os.utils.ui.CompSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class BaseApp extends Application {
    protected String fxmlPath;  // FXML 文件的路径
    protected String IconPath;  // 图标路径
    protected String TitleName;  // 标题名称
    protected double sceneWidth;  // 场景宽度
    protected double sceneHeight;  // 场景高度
    protected Parent root;
    protected FXMLLoader fxmlLoader;

    public BaseApp(String fxmlPath, String IconPath, String TitleName, double sceneWidth, double sceneHeight) {
        this.fxmlPath = fxmlPath;
        this.IconPath = IconPath;
        this.TitleName = TitleName;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }

    @Override
    public void start(Stage stage) throws IOException {
        if (sceneHeight > 0 && sceneWidth > 0) {
            CompSet.setStageSize(stage, sceneWidth, sceneHeight);
        }

        // 获取FXML文件的URL
        URL location = this.getClass().getResource(fxmlPath);
        if (location == null) return;

        // 创建FXML加载器
        fxmlLoader = new FXMLLoader(location);

        // 加载FXML文件并创建根节点
        root = fxmlLoader.load();

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
        setTitleIco(appController, IconPath);

        // 禁止窗口调整大小
        stage.setResizable(false);
        // 设置场景背景为透明
        scene.setFill(Color.TRANSPARENT);
        // 设置窗口样式为透明
        stage.initStyle(StageStyle.TRANSPARENT);

        // 显示窗口
        stage.show();

        // 初始化控制器
        appController.init(stage);
        // 调整控制器窗口
        appController.adaptWindow();
    }

    private void setTitleIco(BaseController bc, String IconPath) {
        URL location = this.getClass().getResource(IconPath);
        ImageView imageView = new ImageView(String.valueOf(location));
        CompSet.setImageViewFixSize(imageView, 20, 20);
        bc.title.setGraphic(imageView);
    }
}
