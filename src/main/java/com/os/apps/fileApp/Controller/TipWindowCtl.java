package com.os.apps.fileApp.Controller;

import com.os.apps.BaseController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TipWindowCtl extends BaseController {
   @FXML
   private Text tipText;

   public void init(Stage stage, String tipString) {
      super.init(stage);
      this.tipText.setText(tipString);
   }
}
