package com.os.applications.fileApp.controller;

import com.os.applications.processControlApp.processSystem.ProcessManager;
import com.os.utility.uiUtil.DrawUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PropertyController extends BaseFileController {
   public Label title;
   public Button acceptButton;
   public RadioButton checkWrite;
   public RadioButton checkRead;
   public Button applyButton;
   public Label typeField;
   public Label locField;
   public Label spaceField;
   public Label timeField;
   public TextField textField;
   public Button cancelButton;
   public HBox titleBarR;
   public Label nameField;
   public Label propertyField;
   public Label propertyLabel;
   public Label propertyLabel1;

   @Override
   public void init(Stage stage) {
      super.init(stage);

      // 创建绘图工具
      DrawUtil drawUtil = new DrawUtil();
      drawUtil.addDrawFunc(stage, this.topMainPane);

      // 监听窗口大小变化，根据窗口大小自适应布局
      stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
      stage.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
      this.adaptWindow();  // 初始时适应窗口
   }
}
