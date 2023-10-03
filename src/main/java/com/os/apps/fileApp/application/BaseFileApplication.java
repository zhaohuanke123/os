package com.os.apps.fileApp.application;

import com.os.apps.BaseApp;
import com.os.apps.BaseController;
import com.os.apps.fileApp.FileApplication;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseFileApplication<T extends BaseController> extends BaseApp<T> {

    public BaseFileApplication(String fxmlPath, String IconPath, String TitleName, double sceneWidth, double sceneHeight) {
        super(fxmlPath, IconPath, TitleName, sceneWidth, sceneHeight);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        controller.showStageToFront();
        FileApplication.fileAppAdditionStageList.add(stage);
    }
}
