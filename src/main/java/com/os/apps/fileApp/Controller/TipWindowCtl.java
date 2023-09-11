package com.os.apps.fileApp.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TipWindowCtl extends BaseFileController{
   @FXML
   private HBox titleBar;
   @FXML
   private Text tipText;

   public void init(Stage stage, String tipString) {
      this.stage = stage;
      this.tipText.setText(tipString);
   }
}
