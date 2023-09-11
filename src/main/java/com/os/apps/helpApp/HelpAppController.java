package com.os.apps.helpApp;

import com.os.apps.BaseController;
import com.os.utils.ui.DrawUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HelpAppController extends BaseController{
    public Label titleBarL;
    public HBox titleBarR;
    @FXML
    private AnchorPane topMainPane;
    @FXML
    private HBox toolBar;
    @FXML
    private TabPane tabPane;
    @Override
    public void init(Stage stage) {
        super.init(stage);

        DrawUtil drawUtil = new DrawUtil();
        drawUtil.addDrawFunc(stage, this.topMainPane);
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(HelpAppController.this::adaptWindow));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(HelpAppController.this::adaptWindow));
        this.adaptWindow();
    }

}
