package com.os.apps.fileApp.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PropertyCtl extends BaseFileController{
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

   public void init(Stage stage) {
      this.stage = stage;
   }
}
