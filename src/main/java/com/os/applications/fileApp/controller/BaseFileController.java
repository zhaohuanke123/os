package com.os.applications.fileApp.controller;

import com.os.applications.BaseController;
import com.os.main.MainController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BaseFileController extends BaseController {

    @Override
    public void init(Stage stage) {
        this.stage = stage;
    }

    @FXML
    @Override
    public void closeStage() {
        super.closeStage();
        MainController.getInstance().appBoxManager.removeAppButton(stage);
    }
}
