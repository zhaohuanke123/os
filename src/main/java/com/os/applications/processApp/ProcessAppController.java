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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class ProcessAppController extends BaseController {
    public AnchorPane mainPane;
    public AnchorPane mainPane1;
    public VBox creatProButtons;
    public Slider creatProSlider;
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
        ProcessScheduleThread.controlButton = this.continueButton;
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
                            Text text = new Text("创建进程失败，创建进程数量已达上限\n");
                            text.setFill(Color.RED);
                            text.setFont(Font.font("宋体"
                                    , 25));
                            tipWindow.controller.tipTextFlow.getChildren().add(text);
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
            } else {
                for (var i : creatProButtons.getChildren()) {
                    if (i instanceof Button) {
                        i.setDisable(false);
                    }
                }
            }
        });

        creatProSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ProcessManager.speed = newValue.intValue();
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
        //获取数据
        DataLoader.processDetailDataLoad(processDetailDataArrayList, (Vector<Process>) updateList, "当前进程");
        Platform.runLater(() ->
                processTable.setItems(FXCollections.observableArrayList(processDetailDataArrayList)));

        //获取数据
        DataLoader.processDetailDataLoad(processDetailDataArrayList1, (Vector<Process>) updateList, "销毁进程");
        Platform.runLater(() ->
                processTable1.setItems(FXCollections.observableArrayList(processDetailDataArrayList1)));

    }

    @FXML
    @Override
    protected void showDescription() {
        super.showDescription();

        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication("", 500, 500);
        try {
            tipWindow.start(stage);
            Text text = new Text("进程管理器\n\n");
            text.setFill(Color.RED);
            text.setFont(Font.font("宋体", 25));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("1. 显示进程的编号、进程状态、执行文件、使用设备、占用内存、进程控制块、当前执行结果、进程完成进度信息。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("2. 默认情况下自动新建进程，也可以选择手动添加新进程。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("3. 可以自由调节进程运行的速度。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("4. 已完成的进程信息会在另一页显示出来。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
