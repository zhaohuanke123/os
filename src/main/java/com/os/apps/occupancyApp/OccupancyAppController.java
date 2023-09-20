package com.os.apps.occupancyApp;

import com.os.apps.BaseController;
import com.os.apps.fileApp.app.MainUI;
import com.os.main.MainController;
import com.os.utils.fileSystem.FAT;
import com.os.utils.process.OccupancyManager;
import com.os.utils.ui.CompSet;
import com.os.utils.ui.DrawUtil;
import javafx.application.Platform;
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
    public AnchorPane mainPane;
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

    private VBox[] boxes1;
    private Button[] textButtons;
    private HBox[] boxes2;

    //endregion
    @Override
    public void init(Stage stage) {
        super.init(stage);

        boxes1 = new VBox[]{this.memoryBox1, this.diskBox1, this.deviceBox1, this.pcbBox1};
        textButtons = new Button[]{this.memoryText, this.diskText, this.deviceText, this.pcbText};
        boxes2 = new HBox[]{this.memoryBox2, this.diskBox2, this.deviceBox2, this.pcbBox2};

        double height;
        double width;
        double percent;
        Region region;

        for (int i = 0; i < OccupancyManager.MEMORY_SIZE; ++i) {
            height = boxes2[0].getHeight();
            width = boxes2[0].getWidth();

            region = new Region();
            percent = (1.0 / OccupancyManager.MEMORY_SIZE);
            CompSet.setCompFixSize(region, percent * width, height);
            this.memoryBox2.getChildren().add(region);
        }

        for (int i = 0; i < FAT.DISK_NUM; ++i) {
            height = boxes2[1].getHeight();
            width = boxes2[1].getWidth();

            region = new Region();
            percent = (1.0 / FAT.DISK_NUM);
            CompSet.setCompFixSize(region, percent * width, height);
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

        MainController.getInstance().uiThread.occupancyAppController = this;
    }

    public void Update() {
        this.occupancyTextUpdate();
        this.occupancyBoxes1Update();
        this.occupancyBoxes2Update();
    }

    private void occupancyTextUpdate() {
        if (textButtons != null) {
            Platform.runLater(() -> {
                int numOfBusyMemory = OccupancyManager.getNumOfBusyMemory();
                double percent = (double) numOfBusyMemory / (double) OccupancyManager.allMemory.length * 100.0;
                String result = String.format("%.2f", percent);
                 textButtons[0].setText(numOfBusyMemory + "B/" + OccupancyManager.allMemory.length + "B(" + result + "%)");

                if (MainUI.fat != null) {
                    int numOfBusyDisk = MainUI.fat.checkNumOfBusyDisk();

                    percent = (double) numOfBusyDisk / (double) FAT.DISK_NUM * 100.0;
                    result = String.format("%.2f", percent);
                     textButtons[1].setText(numOfBusyDisk + "/" + FAT.DISK_NUM + "(" + result + "%)");
                }

                int busyDeviceNum = OccupancyManager.getBusyDeviceNum();
                percent = (double) busyDeviceNum / (double) OccupancyManager.All_DEVICE_SIZE * 100.0;
                result = String.format("%.2f", percent);
                 textButtons[2].setText(busyDeviceNum + "/" + OccupancyManager.All_DEVICE_SIZE + "(" + result + "%)");

                int numOfBusyPcb = 10 - OccupancyManager.freePcbList.size();
                int pcbSize = OccupancyManager.PCB_SIZE;
                percent = (double) numOfBusyPcb / (double) pcbSize * 100.0;
                result = String.format("%.2f", percent);
                 textButtons[3].setText(numOfBusyPcb + "/" + pcbSize + "(" + result + "%)");
            });
        }
    }

    private void occupancyBoxes1Update() {
        if (boxes1 != null) {
            int numOfBusyMemory = OccupancyManager.getNumOfBusyMemory();

            double height = boxes1[0].getHeight() - 2.0;
            double width = boxes1[0].getWidth() - 2.0;
            Region region = (Region) boxes1[0].getChildren().get(0);
            double percent = (double) numOfBusyMemory / (double) OccupancyManager.MEMORY_SIZE;
            CompSet.setCompFixSize(region, width, percent * height);

            if (MainUI.fat != null) {
                int numOfBusyDisk = MainUI.fat.checkNumOfBusyDisk();
                height = boxes1[1].getHeight() - 2.0;
                width = boxes1[1].getWidth() - 2.0;
                percent = (double) numOfBusyDisk / 256.0;
                region = (Region) boxes1[1].getChildren().get(0);
                CompSet.setCompFixSize(region, width, percent * height);

                System.out.println("BusyDiskNum:" + numOfBusyDisk);
            }

            int busyDeviceNum = OccupancyManager.getBusyDeviceNum();
            height = boxes1[2].getHeight() - 2.0;
            width = boxes1[2].getWidth() - 2.0;
            percent = (double) busyDeviceNum / (double) OccupancyManager.All_DEVICE_SIZE;
            region = (Region) boxes1[2].getChildren().get(0);
            CompSet.setCompFixSize(region, width, percent * height);

            int numOfBusyPcb = OccupancyManager.PCB_SIZE - OccupancyManager.freePcbList.size();
            height = boxes1[3].getHeight() - 2.0;
            width = boxes1[3].getWidth() - 2.0;
            percent = (double) numOfBusyPcb / (double) OccupancyManager.PCB_SIZE;
            region = (Region) boxes1[3].getChildren().get(0);
            CompSet.setCompFixSize(region, width, percent * height);
        }
    }

    private void occupancyBoxes2Update() {
        if (boxes2 != null) {
            Platform.runLater(() -> {

                for (int memory_index = 0; memory_index < OccupancyManager.MEMORY_SIZE; ++memory_index) {
                    if (OccupancyManager.allMemory[memory_index] == 0) {
                         boxes2[0].getChildren().get(memory_index).setId("emptyBox");
                    } else {
                         boxes2[0].getChildren().get(memory_index).setId("memoryInBox1");
                    }
                }

                if (MainUI.fat != null) {
                    for (int disk_index = 0; disk_index < FAT.DISK_NUM; ++disk_index) {
                        if (MainUI.fat.getDiskBlocks()[disk_index].getIndex() != 0) {
                             boxes2[1].getChildren().get(disk_index).setId("diskInBox1");
                        } else {
                             boxes2[1].getChildren().get(disk_index).setId("emptyBox");
                        }
                    }
                }

                for (int a_device_index = 0; a_device_index < OccupancyManager.A_DEVICE_SIZE; ++a_device_index) {
                    if (OccupancyManager.aDevice[a_device_index] == 0) {
                         boxes2[2].getChildren().get(a_device_index).setId("emptyBox");
                    } else {
                         boxes2[2].getChildren().get(a_device_index).setId("deviceInBox1");
                    }
                }

                for (int b_device_index = 0; b_device_index < OccupancyManager.B_DEVICE_SIZE; ++b_device_index) {
                    if (OccupancyManager.bDevice[b_device_index] == 0) {
                         boxes2[2].getChildren().get(2 + b_device_index).setId("emptyBox");
                    } else {
                         boxes2[2].getChildren().get(2 + b_device_index).setId("deviceInBox1");
                    }
                }

                for (int c_device_index = 0; c_device_index < OccupancyManager.C_DEVICE_SIZE; ++c_device_index) {
                    if (OccupancyManager.cDevice[c_device_index] == 0) {
                         boxes2[2].getChildren().get(5 + c_device_index).setId("emptyBox");
                    } else {
                         boxes2[2].getChildren().get(5 + c_device_index).setId("deviceInBox1");
                    }
                }

                int i, pcbId;
                for (i = 0; i < OccupancyManager.PCB_SIZE; ++i) {
                     boxes2[3].getChildren().get(i).setId("emptyBox");
                }

                for (i = 0; i < MainController.getInstance().uiThread.creatingProcessList.size(); ++i) {
                    pcbId = MainController.getInstance().uiThread.creatingProcessList.get(i).pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                         boxes2[3].getChildren().get(pcbId).setId("pcbInBox0");
                    }
                }

                for (i = 0; i < MainController.getInstance().uiThread.waitProcessList.size(); ++i) {
                    pcbId = MainController.getInstance().uiThread.waitProcessList.get(i).pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                         boxes2[3].getChildren().get(pcbId).setId("pcbInBox1");
                    }
                }

                if (MainController.getInstance().uiThread.runProcess != null) {
                    pcbId = MainController.getInstance().uiThread.runProcess.pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                         boxes2[3].getChildren().get(pcbId).setId("pcbInBox2");
                    }
                }

                for (i = 0; i < MainController.getInstance().uiThread.blockProcessList.size(); ++i) {
                    pcbId = MainController.getInstance().uiThread.blockProcessList.get(i).pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                         boxes2[3].getChildren().get(pcbId).setId("pcbInBox3");
                    }
                }

            });
        }
    }
}
