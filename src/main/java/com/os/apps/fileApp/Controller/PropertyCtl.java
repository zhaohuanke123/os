package com.os.apps.fileApp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PropertyCtl {
    public HBox titleBar;
   public Label propertyIcon;
   public Button yes;
   public RadioButton write;
   public RadioButton read;
   public Button apply;
   public Label type;
   public Label loc;
   public Label space;
   public Label time;
   public TextField na;
   public Button no;
   Stage stage;
   double xOffset;
   double yOffset;

   public void init(Stage stage) {
      this.stage = stage;
   }

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
}
