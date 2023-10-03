package com.os.apps.fileApp.controller;

import com.os.apps.BaseController;
import javafx.stage.Stage;

public class BaseFileController extends BaseController {

    @Override
    public void init(Stage stage) {
        this.stage = stage;
    }
}
