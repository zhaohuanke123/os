package com.os.applications.fileApp.application;

import com.os.applications.BaseApp;
import com.os.applications.BaseController;
import com.os.main.MainController;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseFileApplication<T extends BaseController> extends BaseApp<T> {
    protected String toolTip;

    public BaseFileApplication(String fxmlPath, String IconPath, String TitleName, double sceneWidth, double sceneHeight) {
        super(fxmlPath, IconPath, TitleName, sceneWidth, sceneHeight);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        // 将窗口置于其他窗口之上
        controller.showStageToFront();
        // 将当前应用程序的窗口添加到应用程序窗口列表中

        FileApplication.fileAppAdditionStageList.add(stage);
        FileApplication.fileAppAdditionControllerList.add(controller);

        // 创建一个按钮用于表示应用程序
        Button button = new Button();
        // 设置按钮图标为应用程序的图标
        button.setGraphic(this.getIco());
        // 在主窗口中添加应用程序按钮，显示 toolTip
        MainController.getInstance().appBoxManager.addAppButton(button, stage, toolTip);
    }
}
