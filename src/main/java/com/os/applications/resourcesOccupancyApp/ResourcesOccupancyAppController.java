package com.os.applications.resourcesOccupancyApp;

import com.os.applications.BaseController;
import com.os.applications.fileApp.application.FileApplication;
import com.os.applications.fileApp.application.TipDialogApplication;
import com.os.main.MainController;
import com.os.utility.fileSystem.FAT;
import com.os.applications.resourcesOccupancyApp.models.ResourcesOccupancyManager;
import com.os.utility.uiUtil.CompSet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ResourcesOccupancyAppController extends BaseController {
    //region [FXML comp variables]
    public AnchorPane topMainPane;
    public Label memoryText;
    public Label diskText;
    public Label pcbText;
    public Label deviceText;
    public VBox diskBox1;
    public VBox memoryBox1;
    public VBox deviceBox1;
    public VBox pcbBox1;
    public HBox memoryBox2;
    public HBox deviceBox2;
    public HBox diskBox2;
    public HBox pcbBox2;
    public LineChart<String, Number> memoryChart;
    public LineChart<String, Number> deviceChart;
    public LineChart<String, Number> diskChart;
    public LineChart<String, Number> pcbChart;
    public Button DescriptionBtn;
    private ChartBean[] chartBeans;

    private VBox[] boxes1;
    private Label[] textLabel;
    private HBox[] boxes2;

    //endregion
    @Override
    public void init(Stage stage) {
//        super.init(stage);
        this.stage = stage;

        boxes1 = new VBox[]{this.memoryBox1, this.diskBox1, this.deviceBox1, this.pcbBox1};
        textLabel = new Label[]{this.memoryText, this.diskText, this.deviceText, this.pcbText};
        boxes2 = new HBox[]{this.memoryBox2, this.diskBox2, this.deviceBox2, this.pcbBox2};

        double height;
        double width;
        double percent;
        Region region;

        for (int i = 0; i < ResourcesOccupancyManager.MEMORY_SIZE; ++i) {
            height = boxes2[0].getHeight();
            width = boxes2[0].getWidth();

            region = new Region();
            percent = (1.0 / ResourcesOccupancyManager.MEMORY_SIZE);
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
        for (int i = 0; i < ResourcesOccupancyManager.All_DEVICE_SIZE; ++i) {
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

            percent = (1.0 / ResourcesOccupancyManager.All_DEVICE_SIZE);
            label.setPrefSize(percent * width, height);
            label.setMinSize(percent * width, height);
            label.setMaxSize(percent * width, height);
            label.setAlignment(Pos.CENTER);
            this.deviceBox2.getChildren().add(label);
        }

        for (int i = 0; i < ResourcesOccupancyManager.PCB_SIZE; ++i) {
            height = boxes2[3].getHeight();
            width = boxes2[3].getWidth();
            label = new Label();
            label.setId("emptyBox");
            label.setText(i + "");

            percent = (1.0 / ResourcesOccupancyManager.PCB_SIZE);
            label.setPrefSize(percent * width, height);
            label.setMinSize(percent * width, height);
            label.setMaxSize(percent * width, height);
            label.setAlignment(Pos.CENTER);
            this.pcbBox2.getChildren().add(label);
        }

        MainController.getInstance().uiUpdateThread.occupancyAppController = this;

        chartBeans = new ChartBean[]{
                new ChartBean(memoryChart, "内存使用情况/%", "时间"),
                new ChartBean(diskChart, "磁盘使用情况/%", "时间"),
                new ChartBean(deviceChart, "设备使用情况/%", "时间"),
                new ChartBean(pcbChart, "进程使用情况/%", "时间")
        };
        for (ChartBean chartBean : chartBeans) {
            chartBean.chart.prefWidthProperty().bind(this.topMainPane.widthProperty());
        }
    }

    public void Update() {
        this.occupancyTextUpdate();
        this.occupancyBoxes1Update();
        this.occupancyBoxes2Update();
    }

    private void occupancyTextUpdate() {
        if (textLabel != null) {
            Platform.runLater(() -> {
                int numOfBusyMemory = ResourcesOccupancyManager.getNumOfBusyMemory();
                double percent = (double) numOfBusyMemory / (double) ResourcesOccupancyManager.allMemory.length * 100.0;
                String result = String.format("%.2f", percent);
                textLabel[0].setText(numOfBusyMemory + "B/" + ResourcesOccupancyManager.allMemory.length + "B(" + result + "%)");

                chartBeans[0].addData(MainController.getInstance().uiUpdateThread.time,
                        numOfBusyMemory * 100 / ResourcesOccupancyManager.allMemory.length);

                if (FileApplication.fat != null) {
                    int numOfBusyDisk = FileApplication.fat.checkNumOfBusyDisk();

                    percent = (double) numOfBusyDisk / (double) FAT.DISK_NUM * 100.0;
                    result = String.format("%.2f", percent);
                    textLabel[1].setText(numOfBusyDisk + "/" + FAT.DISK_NUM + "(" + result + "%)");

                    chartBeans[1].addData(MainController.getInstance().uiUpdateThread.time,
                            numOfBusyDisk * 100 / FAT.DISK_NUM);
                }

                int busyDeviceNum = ResourcesOccupancyManager.getBusyDeviceNum();
                percent = (double) busyDeviceNum / (double) ResourcesOccupancyManager.All_DEVICE_SIZE * 100.0;
                result = String.format("%.2f", percent);
                textLabel[2].setText(busyDeviceNum + "/" + ResourcesOccupancyManager.All_DEVICE_SIZE + "(" + result + "%)");

                chartBeans[2].addData(MainController.getInstance().uiUpdateThread.time,
                        busyDeviceNum * 100 / ResourcesOccupancyManager.All_DEVICE_SIZE);

                int numOfBusyPcb = 10 - ResourcesOccupancyManager.freePcbList.size();
                int pcbSize = ResourcesOccupancyManager.PCB_SIZE;
                percent = (double) numOfBusyPcb / (double) pcbSize * 100.0;
                result = String.format("%.2f", percent);
                textLabel[3].setText(numOfBusyPcb + "/" + pcbSize + "(" + result + "%)");

                chartBeans[3].addData(MainController.getInstance().uiUpdateThread.time,
                        numOfBusyPcb * 100 / pcbSize);
            });
        }
    }

    private void occupancyBoxes1Update() {
        if (boxes1 != null) {
            int numOfBusyMemory = ResourcesOccupancyManager.getNumOfBusyMemory();

            double height = boxes1[0].getHeight() - 2.0;
            double width = boxes1[0].getWidth() - 2.0;
            Region region = (Region) boxes1[0].getChildren().get(0);
            double percent = (double) numOfBusyMemory / (double) ResourcesOccupancyManager.MEMORY_SIZE;
            CompSet.setCompFixSize(region, width, percent * height);

            if (FileApplication.fat != null) {
                int numOfBusyDisk = FileApplication.fat.checkNumOfBusyDisk();
                height = boxes1[1].getHeight() - 2.0;
                width = boxes1[1].getWidth() - 2.0;
                percent = (double) numOfBusyDisk / 256.0;
                region = (Region) boxes1[1].getChildren().get(0);
                CompSet.setCompFixSize(region, width, percent * height);
            }

            int busyDeviceNum = ResourcesOccupancyManager.getBusyDeviceNum();
            height = boxes1[2].getHeight() - 2.0;
            width = boxes1[2].getWidth() - 2.0;
            percent = (double) busyDeviceNum / (double) ResourcesOccupancyManager.All_DEVICE_SIZE;
            region = (Region) boxes1[2].getChildren().get(0);
            CompSet.setCompFixSize(region, width, percent * height);

            int numOfBusyPcb = ResourcesOccupancyManager.PCB_SIZE - ResourcesOccupancyManager.freePcbList.size();
            height = boxes1[3].getHeight() - 2.0;
            width = boxes1[3].getWidth() - 2.0;
            percent = (double) numOfBusyPcb / (double) ResourcesOccupancyManager.PCB_SIZE;
            region = (Region) boxes1[3].getChildren().get(0);
            CompSet.setCompFixSize(region, width, percent * height);
        }
    }

    private void occupancyBoxes2Update() {
        if (boxes2 != null) {
            Platform.runLater(() -> {

                for (int memory_index = 0; memory_index < ResourcesOccupancyManager.MEMORY_SIZE; ++memory_index) {
                    if (ResourcesOccupancyManager.allMemory[memory_index] == 0) {
                        boxes2[0].getChildren().get(memory_index).setId("emptyBox");
                    } else {
                        boxes2[0].getChildren().get(memory_index).setId("memoryInBox1");
                    }
                }

                if (FileApplication.fat != null) {
                    for (int disk_index = 0; disk_index < FAT.DISK_NUM; ++disk_index) {
                        if (FileApplication.fat.getDiskBlocks()[disk_index].getIndex() != 0) {
                            boxes2[1].getChildren().get(disk_index).setId("diskInBox1");
                        } else {
                            boxes2[1].getChildren().get(disk_index).setId("emptyBox");
                        }
                    }
                }

                for (int a_device_index = 0; a_device_index < ResourcesOccupancyManager.A_DEVICE_SIZE; ++a_device_index) {
                    if (ResourcesOccupancyManager.aDevice[a_device_index] == 0) {
                        boxes2[2].getChildren().get(a_device_index).setId("emptyBox");
                    } else {
                        boxes2[2].getChildren().get(a_device_index).setId("deviceInBox1");
                    }
                }

                for (int b_device_index = 0; b_device_index < ResourcesOccupancyManager.B_DEVICE_SIZE; ++b_device_index) {
                    if (ResourcesOccupancyManager.bDevice[b_device_index] == 0) {
                        boxes2[2].getChildren().get(2 + b_device_index).setId("emptyBox");
                    } else {
                        boxes2[2].getChildren().get(2 + b_device_index).setId("deviceInBox1");
                    }
                }

                for (int c_device_index = 0; c_device_index < ResourcesOccupancyManager.C_DEVICE_SIZE; ++c_device_index) {
                    if (ResourcesOccupancyManager.cDevice[c_device_index] == 0) {
                        boxes2[2].getChildren().get(5 + c_device_index).setId("emptyBox");
                    } else {
                        boxes2[2].getChildren().get(5 + c_device_index).setId("deviceInBox1");
                    }
                }

                int i, pcbId;
                for (i = 0; i < ResourcesOccupancyManager.PCB_SIZE; ++i) {
                    boxes2[3].getChildren().get(i).setId("emptyBox");
                }

                // 新建态
                for (i = 0; i < MainController.getInstance().uiUpdateThread.creatingProcessList.size(); ++i) {
                    pcbId = MainController.getInstance().uiUpdateThread.creatingProcessList.get(i).pcbID;
                    if (ResourcesOccupancyManager.checkPCBIndex(pcbId)) {
                        boxes2[3].getChildren().get(pcbId).setId("pcbInBox0");
                    }
                }

                // 就绪态
                for (i = 0; i < MainController.getInstance().uiUpdateThread.waitProcessList.size(); ++i) {
                    pcbId = MainController.getInstance().uiUpdateThread.waitProcessList.get(i).pcbID;
                    if (ResourcesOccupancyManager.checkPCBIndex(pcbId)) {
                        boxes2[3].getChildren().get(pcbId).setId("pcbInBox1");
                    }
                }

                //运行态
                if (MainController.getInstance().uiUpdateThread.runProcess != null) {
                    pcbId = MainController.getInstance().uiUpdateThread.runProcess.pcbID;
                    if (ResourcesOccupancyManager.checkPCBIndex(pcbId)) {
                        boxes2[3].getChildren().get(pcbId).setId("pcbInBox2");
                    }
                }

                // 阻塞态
                for (i = 0; i < MainController.getInstance().uiUpdateThread.blockProcessList.size(); ++i) {
                    pcbId = MainController.getInstance().uiUpdateThread.blockProcessList.get(i).pcbID;
                    if (ResourcesOccupancyManager.checkPCBIndex(pcbId)) {
                        boxes2[3].getChildren().get(pcbId).setId("pcbInBox3");
                    }
                }

            });
        }
    }

    static class ChartBean {
        LineChart<String, Number> chart;
        XYChart.Series<String, Number> dataSeries;

        public ChartBean(LineChart<String, Number> chart, String yLabel, String xLabel) {
            this.chart = chart;
            this.dataSeries = new XYChart.Series<>();
            this.dataSeries.setName(yLabel);
            this.chart.getXAxis().setLabel(xLabel);
            this.chart.getYAxis().setLabel(yLabel);
            chart.getData().add(dataSeries);

            chart.getYAxis().setAutoRanging(false);
            ((NumberAxis) chart.getYAxis()).setUpperBound(102);
            ((NumberAxis) chart.getYAxis()).setLowerBound(0);
        }

        public void addData(Number x, Number y) {
            dataSeries.getData().add(new XYChart.Data<>(String.valueOf(x), y));
            if (dataSeries.getData().size() > 15)
                dataSeries.getData().remove(0);
        }
    }

    @FXML
    @Override
    protected void showDescription() {
        super.showDescription();

        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication("", 500,500);
        try {
            tipWindow.start(stage);
            Text text = new Text("占用管理器\n\n");
            text.setFill(Color.RED);
            text.setFont(Font.font("宋体", 25));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("1. 显示内存的占用比和已使用内存的分布情况。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("2. 显示设备的占用比和已使用的设备情况。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("3. 显示磁盘的占用比和已使用磁盘的分布情况。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("4. 显示PCB的占用比和已使用的PCB情况。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
