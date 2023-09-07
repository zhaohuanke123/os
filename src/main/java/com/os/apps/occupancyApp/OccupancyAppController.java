package com.os.apps.occupancyApp;

import com.os.apps.BaseController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.os.utils.ui.DrawUtil;
import com.os.utils.ui.UIThread;

public class OccupancyAppController extends BaseController{
    public Label titleBarL;
   public HBox titleBarR;
   public AnchorPane mainPane;
   @FXML
   private AnchorPane topMainPane;
   @FXML
   private Button memoryText;
   @FXML
   private Button diskText;
   @FXML
   private Button pcbText;
   @FXML
   private Button deviceText;
   @FXML
   private VBox diskBox1;
   @FXML
   private VBox memoryBox1;
   @FXML
   private VBox deviceBox1;
   @FXML
   private VBox pcbBox1;
   @FXML
   private HBox memoryBox2;
   @FXML
   private HBox deviceBox2;
   @FXML
   private HBox diskBox2;
   @FXML
   private HBox pcbBox2;

   public void init(Stage stage) {
      this.stage = stage;
      VBox[] boxes1 = new VBox[]{this.memoryBox1, this.diskBox1, this.deviceBox1, this.pcbBox1};
      Button[] textButtons = new Button[]{this.memoryText, this.diskText, this.deviceText, this.pcbText};
      HBox[] boxes2 = new HBox[]{this.memoryBox2, this.diskBox2, this.deviceBox2, this.pcbBox2};

      int i;
      double height;
      double width;
      double percent;
      Region region;
      for(i = 0; i < 512; ++i) {
         height = boxes2[0].getHeight();
         width = boxes2[0].getWidth();
         percent = 0.00185546875;
         region = new Region();
         region.setPrefSize(percent * width, height);
         region.setMinSize(percent * width, height);
         region.setMaxSize(percent * width, height);
         this.memoryBox2.getChildren().add(region);
      }

      for(i = 0; i < 256; ++i) {
         height = boxes2[1].getHeight();
         width = boxes2[1].getWidth();
         percent = 0.0037109375;
         region = new Region();
         region.setPrefSize(percent * width, height);
         region.setMinSize(percent * width, height);
         region.setMaxSize(percent * width, height);
         this.diskBox2.getChildren().add(region);
      }

      Label label;
      for(i = 0; i < 8; ++i) {
         height = boxes2[2].getHeight();
         width = boxes2[3].getWidth();
         percent = 0.12375;
         label = new Label();
         label.setId("emptyBox");
         if (i >= 0 && i <= 1) {
            label.setText("A");
         } else if (i >= 2 && i <= 4) {
            label.setText("B");
         } else {
            label.setText("C");
         }

         label.setPrefSize(percent * width, 0.9 * height);
         label.setMinSize(percent * width, 0.9 * height);
         label.setMaxSize(percent * width, 0.9 * height);
         label.setAlignment(Pos.CENTER);
         this.deviceBox2.getChildren().add(label);
      }

      for(i = 0; i < 10; ++i) {
         height = boxes2[2].getHeight();
         width = boxes2[3].getWidth();
         percent = 0.099;
         label = new Label();
         label.setId("emptyBox");
         label.setText(i + "");
         label.setPrefSize(percent * width, 0.9 * height);
         label.setMinSize(percent * width, 0.9 * height);
         label.setMaxSize(percent * width, 0.9 * height);
         label.setAlignment(Pos.CENTER);
         this.pcbBox2.getChildren().add(label);
      }

      DrawUtil drawUtil = new DrawUtil();
      drawUtil.addDrawFunc(stage, this.topMainPane);
      UIThread.boxes1 = boxes1;
      UIThread.textButtons = textButtons;
      UIThread.boxes2 = boxes2;
   }
}
