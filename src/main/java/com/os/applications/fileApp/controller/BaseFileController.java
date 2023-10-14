package com.os.applications.fileApp.controller;

import com.os.applications.BaseController;
import javafx.stage.Stage;

public class BaseFileController extends BaseController {

    @Override
    public void init(Stage stage) {
        this.stage = stage;
    }
}
