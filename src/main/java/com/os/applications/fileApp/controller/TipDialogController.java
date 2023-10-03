package com.os.applications.fileApp.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TipDialogController extends BaseFileController {
   @FXML
   private Text tipText;

   public void init(Stage stage, String tipString) {
      super.init(stage);
      this.tipText.setText(tipString);
   }
}
