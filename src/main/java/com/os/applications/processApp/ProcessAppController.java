package com.os.applications.processApp;

import com.os.applications.BaseController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class ProcessAppController extends BaseController {
    public AnchorPane mainPane;
    public AnchorPane mainPane1;
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

        MainController.getInstance().uiThread.processAppController = this;
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
}
