package com.os.applications.processApp;

import com.os.applications.BaseController;
import com.os.applications.fileApp.FileApplicationController;
import com.os.applications.fileApp.application.TipDialogApplication;
import com.os.dataModels.InstructionData;
import com.os.applications.processApp.models.ProcessDetailData;
import com.os.main.MainController;
import com.os.utility.DataLoader;
import com.os.applications.processApp.processSystem.Process;
import com.os.applications.processApp.processSystem.ProcessManager;
import com.os.applications.processApp.processSystem.ProcessScheduleThread;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class ProcessAppController extends BaseController {
    public AnchorPane mainPane;
    public AnchorPane mainPane1;
    public VBox creatProButtons;
    @FXML
    private TableView<ProcessDetailData> processTable;
    @FXML
    private TableView<ProcessDetailData> processTable1;
    @FXML
    private TableColumn<?, ?> processName;
    @FXML
    private TableColumn<?, ?> processState;
    @FXML
    private TableColumn<?, ?> whichFile;
    @FXML
    private TableColumn<?, ?> havedDevice;
    @FXML
    private TableColumn<?, ?> havedMemory;
    @FXML
    private TableColumn<?, ?> havedPid;
    @FXML
    private TableColumn<?, ?> result;
    @FXML
    private TableColumn<?, ?> progressBar;
    @FXML
    private TableColumn<?, ?> processName1;
    @FXML
    private TableColumn<?, ?> processState1;
    @FXML
    private TableColumn<?, ?> whichFile1;
    @FXML
    private TableColumn<?, ?> havedDevice1;
    @FXML
    private TableColumn<?, ?> havedMemory1;
    @FXML
    private TableColumn<?, ?> havedPid1;
    @FXML
    private TableColumn<?, ?> result1;
    @FXML
    private TableColumn<?, ?> progressBar1;
    @FXML
    private CheckBox continueButton;
    @FXML
    private CheckBox suspendButton;

    //继续新建进程/停止新建进程
    @FXML
    void createSelectByMouse(MouseEvent event) {
        this.createSelect((CheckBox) event.getSource());
    }

    void createSelect(CheckBox checkBox) {
        CheckBox[] array = new CheckBox[]{this.continueButton, this.suspendButton};

        for (CheckBox box : array) {
            box.setSelected(checkBox == box);
        }

        checkBox.setSelected(true);
    }

    //初始化
    @Override
    public void init(Stage stage) {
        super.init(stage);

        //显示面板
        this.processName.setCellValueFactory(new PropertyValueFactory<>("processName"));
        this.processState.setCellValueFactory(new PropertyValueFactory<>("processState"));
        this.whichFile.setCellValueFactory(new PropertyValueFactory<>("whichFile"));
        this.havedDevice.setCellValueFactory(new PropertyValueFactory<>("havedDevice"));
        this.havedMemory.setCellValueFactory(new PropertyValueFactory<>("havedMemory"));
        this.havedPid.setCellValueFactory(new PropertyValueFactory<>("havedPid"));
        this.result.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.progressBar.setCellValueFactory(new PropertyValueFactory<>("progressBar"));
        //this.instruction.setCellValueFactory(new PropertyValueFactory<>("instruction"));

        //显示面板
        this.processName1.setCellValueFactory(new PropertyValueFactory<>("processName"));
        this.processState1.setCellValueFactory(new PropertyValueFactory<>("processState"));
        this.whichFile1.setCellValueFactory(new PropertyValueFactory<>("whichFile"));
        this.havedDevice1.setCellValueFactory(new PropertyValueFactory<>("havedDevice"));
        this.havedMemory1.setCellValueFactory(new PropertyValueFactory<>("havedMemory"));
        this.havedPid1.setCellValueFactory(new PropertyValueFactory<>("havedPid"));
        this.result1.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.progressBar1.setCellValueFactory(new PropertyValueFactory<>("progressBar"));

        //继续或者停止新建
        ProcessScheduleThread.controlButton = new CheckBox[]{this.continueButton, this.suspendButton};
        continueButton.setSelected(true);

        for (var i : ProcessScheduleThread.executableFileList) {
            Button button = new Button(i.getName());
            button.setDisable(true);
            Tooltip tooltip = new Tooltip(i.toString());
            button.setTooltip(tooltip);
            creatProButtons.getChildren().add(button);
            creatProButtons.setSpacing(10);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    if (!ProcessScheduleThread.executableFileList.isEmpty()) {
                        if (ProcessScheduleThread.creatingProcessList.size() < 3) {
                            Process newProcess = new Process(ProcessScheduleThread.processNum, i, i.id);
                            ProcessScheduleThread.creatingProcessList.add(newProcess);
                            ProcessScheduleThread.allProcessList.add(newProcess);
                            ++ProcessScheduleThread.processNum;
                            newProcess.Create();
                        } else {
                            TipDialogApplication tipWindow = new TipDialogApplication("创建进程失败，创建进程数量已达上限", 500, 500);
                            try {
                                tipWindow.start(new Stage());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        }

        continueButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (var i : creatProButtons.getChildren()) {
                    if (i instanceof Button) {
                        i.setDisable(true);
                    }
                }
            }
        });

        suspendButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (var i : creatProButtons.getChildren()) {
                    if (i instanceof Button) {
                        i.setDisable(false);
                    }
                }
            }
        });

        MainController.getInstance().uiThread.processAppController = this;
        ProcessScheduleThread.processAppController = this;
    }

    public void Update() {
        this.processTableUpdate();
    }

    public ArrayList<ProcessDetailData> processDetailDataArrayList = new ArrayList<>();
    public ArrayList<ProcessDetailData> processDetailDataArrayList1 = new ArrayList<>();

    //进行数据更新
    private void processTableUpdate() {

        Vector<?> updateList = (Vector<?>) ProcessManager.allProcessList.clone();
        //$$$获取数据
        DataLoader.processDetailDataLoad(processDetailDataArrayList, (Vector<Process>) updateList, "当前进程");
        Platform.runLater(() ->
                processTable.setItems(FXCollections.observableArrayList(processDetailDataArrayList)));

        //$$$获取数据
        DataLoader.processDetailDataLoad(processDetailDataArrayList1, (Vector<Process>) updateList, "销毁进程");
        Platform.runLater(() ->
                processTable1.setItems(FXCollections.observableArrayList(processDetailDataArrayList1)));

    }

    @FXML
    @Override
    protected void showDescription() {
        super.showDescription();

        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication("进程管理，主要作用是可视化进程的运行情况. " +
                "1）单独显示当前运行进程的编号、执行指令、数据寄存器的值、剩余时间片。[默认时间片为6]。" +
                " 2）显示当前进程的执行进度，高亮当前执行指令。" +
                "3）显示进程详表，具体包括：进程编号、进程状态、执行文件、设备使用情况、进程控制块、当前执行结果、进程完成进度。" +
                "\"\n", 500, 500);
        try {
            tipWindow.start(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
