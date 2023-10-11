package com.os.applications.fileApp.application;

import com.os.applications.BaseApp;
import com.os.applications.BaseController;
import com.os.applications.fileApp.FileApplication;
import com.os.main.MainController;
import com.os.utility.uiUtil.CompSet;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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

        controller.showStageToFront();
        FileApplication.fileAppAdditionStageList.add(stage);

        Button button = new Button();
        button.setGraphic(this.getIco());
        MainController.getInstance().appBoxManager.addAppButton(button, stage, toolTip);
    }
}
