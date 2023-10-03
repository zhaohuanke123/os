package com.os.applications.fileApp.application;

import com.os.applications.BaseApp;
import com.os.applications.BaseController;
import com.os.applications.fileApp.FileApplication;
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
