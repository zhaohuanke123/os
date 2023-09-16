package com.os.apps.occupancyApp;

import com.os.apps.BaseController;
import com.os.utils.fileSystem.FAT;
import com.os.utils.process.OccupancyManager;
import com.os.utils.ui.CompSet;
import com.os.utils.ui.DrawUtil;
import com.os.utils.ui.UIThread;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OccupancyAppController extends BaseController {
    //region [FXML comp variables]
    public Label titleBarL;
    public HBox titleBarR;
    public AnchorPane mainPane;
    public AnchorPane topMainPane;
    public Button memoryText;
    public Button diskText;
    public Button pcbText;
    public Button deviceText;
    public VBox diskBox1;
    public VBox memoryBox1;
    public VBox deviceBox1;
    public VBox pcbBox1;
    public HBox memoryBox2;
    public HBox deviceBox2;
    public HBox diskBox2;
    public HBox pcbBox2;

    //endregion
    @Override
    public void init(Stage stage) {
        super.init(stage);

        VBox[] boxes1 = new VBox[]{this.memoryBox1, this.diskBox1, this.deviceBox1, this.pcbBox1};
        Button[] textButtons = new Button[]{this.memoryText, this.diskText, this.deviceText, this.pcbText};
        HBox[] boxes2 = new HBox[]{this.memoryBox2, this.diskBox2, this.deviceBox2, this.pcbBox2};

        double height;
        double width;
        double percent;
        Region region;

        for (int i = 0; i < OccupancyManager.MEMORY_SIZE; ++i) {
            height = boxes2[0].getHeight();
            width = boxes2[0].getWidth();

            region = new Region();
            percent = (1.0 / OccupancyManager.MEMORY_SIZE);
            CompSet.setCompSize(region, percent * width, height);
            this.memoryBox2.getChildren().add(region);
        }

        for (int i = 0; i < FAT.DISK_NUM; ++i) {
            height = boxes2[1].getHeight();
            width = boxes2[1].getWidth();

            region = new Region();
            percent = (1.0 / FAT.DISK_NUM);
            CompSet.setCompSize(region, percent * width, height);
            this.diskBox2.getChildren().add(region);
        }

        Label label;
        for (int i = 0; i < OccupancyManager.All_DEVICE_SIZE; ++i) {
            height = boxes2[2].getHeight();
            width = boxes2[2].getWidth();

            label = new Label();
            label.setId("emptyBox");
            if (i <= 1) {
                label.setText("A");
            } else if (i <= 4) {
                label.setText("B");
            } else {
                label.setText("C");
            }

            percent = (1.0 / OccupancyManager.All_DEVICE_SIZE);
            label.setPrefSize(percent * width, height);
            label.setMinSize(percent * width, height);
            label.setMaxSize(percent * width, height);
            label.setAlignment(Pos.CENTER);
            this.deviceBox2.getChildren().add(label);
        }

        for (int i = 0; i < OccupancyManager.PCB_SIZE; ++i) {
            height = boxes2[3].getHeight();
            width = boxes2[3].getWidth();
            label = new Label();
            label.setId("emptyBox");
            label.setText(i + "");

            percent = (1.0 / OccupancyManager.PCB_SIZE);
            label.setPrefSize(percent * width, height);
            label.setMinSize(percent * width, height);
            label.setMaxSize(percent * width, height);
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
