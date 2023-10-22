package com.os.applications.fileApp.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TipDialogController extends BaseFileController {
    public BorderPane contentPane;
    public TextFlow tipTextFlow;
    @FXML
    private Text tipText;

    public void init(Stage stage, String tipString) {
        super.init(stage);
//        this.tipText.setText(tipString);

//        contentPane.setPrefWidth(stage.getWidth());
//        tipText.setWrappingWidth(stage.getWidth() - 20);
    }
}
