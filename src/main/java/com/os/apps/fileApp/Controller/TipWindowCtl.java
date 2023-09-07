package com.os.apps.fileApp.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TipWindowCtl {
   @FXML
   private HBox titleBar;
   @FXML
   private Text tipText;
   double xOffset = 0.0;
   double yOffset = 0.0;
   Stage stage;

   @FXML
   void closeStage(MouseEvent event) {
      this.stage.close();
   }

   @FXML
   void minimizeStage(MouseEvent event) {
      this.stage.setIconified(true);
   }

   @FXML
   void pressBar(MouseEvent event) {
      this.xOffset = event.getSceneX();
      this.yOffset = event.getSceneY();
   }

   @FXML
   void dragBar(MouseEvent event) {
      this.stage.setX(event.getScreenX() - this.xOffset);
      this.stage.setY(event.getScreenY() - this.yOffset);
//      this.stage.setOpacity(0.800000011920929);
   }

   @FXML
   void dragBarDone(DragEvent event) {
      this.stage.setOpacity(1.0);
   }

   @FXML
   void releaseBar(MouseEvent event) {
      this.stage.setOpacity(1.0);
   }

   public void init(Stage stage, String tipString) {
      this.stage = stage;
      this.tipText.setText(tipString);
   }
}
