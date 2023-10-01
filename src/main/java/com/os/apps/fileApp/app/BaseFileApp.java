package com.os.apps.fileApp.app;

import com.os.apps.BaseApp;
import com.os.apps.BaseController;
import com.os.apps.fileApp.FileApp;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseFileApp<T extends BaseController> extends BaseApp<T> {

    public BaseFileApp(String fxmlPath, String IconPath, String TitleName, double sceneWidth, double sceneHeight) {
        super(fxmlPath, IconPath, TitleName, sceneWidth, sceneHeight);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        controller.showStageToFront();
        FileApp.fileAppAdditionStageList.add(stage);
    }
}
