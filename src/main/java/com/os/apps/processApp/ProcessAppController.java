package com.os.apps.processApp;

import com.os.apps.BaseController;
import com.os.datas.InstructionData;
import com.os.datas.ProcessDetailData;
import com.os.main.MainController;
import com.os.utils.DataLoader;
import com.os.utils.process.Process;
import com.os.utils.ui.DrawUtil;
import com.os.utils.process.ProcessManager;
import com.os.utils.process.ProcessScheduleThread;
import com.os.utils.ui.UIThread;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Vector;

public class ProcessAppController extends BaseController {
    public AnchorPane mainPane;
    public Label titleBarL;
    public HBox titleBarR;
    @FXML
    private AnchorPane topMainPane;
    @FXML
    private TableView<ProcessDetailData> processTable;
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
    private Button nowProcessName;
    @FXML
    private Button nowResult;
    @FXML
    private Button nowInstruction;
    @FXML
    private TableView<InstructionData> nowProcessTable;
    @FXML
    private TableColumn<?, ?> instruction;
    @FXML
    private CheckBox showNow;
    @FXML
    private CheckBox showCreating;
    @FXML
    private CheckBox showWaiting;
    @FXML
    private CheckBox showBlocked;
    @FXML
    private CheckBox showEnded;
    @FXML
    private CheckBox showAll;
    @FXML
    private CheckBox signEnded;
    @FXML
    private CheckBox signCreating;
    @FXML
    private CheckBox signWaiting;
    @FXML
    private CheckBox signBlocked;
    @FXML
    private CheckBox signRunning;
    @FXML
    private CheckBox continueButton;
    @FXML
    private CheckBox suspendButton;
    @FXML
    private Button residueSlice;
    @FXML
    private RadioButton speed1Button;
    @FXML
    private RadioButton speed2Button;
    @FXML
    private RadioButton speed4Button;
    @FXML
    private RadioButton speed8Button;
    private CheckBox[] checkBoxes1;
    private CheckBox[] checkBoxes2;

    @FXML
    void createSelectByMouse(MouseEvent event) {
        this.createSelect((CheckBox) event.getSource());
    }

    void createSelect(CheckBox checkBox) {
        CheckBox[] array = new CheckBox[]{this.continueButton, this.suspendButton};

        for (int i = 0; i < 2; ++i) {
            array[i].setSelected(checkBox == array[i]);
        }

        System.out.println(checkBox.getId() + "被选中");
        checkBox.setSelected(true);
    }

    @FXML
    void showSelectByMouse(MouseEvent event) {
        this.showSelect((CheckBox) event.getSource());
    }

    void showSelect(CheckBox checkBox) {
        CheckBox[] array = new CheckBox[]{this.showNow, this.showCreating, this.showWaiting, this.showBlocked, this.showEnded, this.showAll};

        for (int i = 0; i < 6; ++i) {
            array[i].setSelected(checkBox == array[i]);
        }

        System.out.println(checkBox.getId() + "被选中");
        checkBox.setSelected(true);
    }

    @FXML
    void signSelectByMouse(MouseEvent event) {
        this.signSelect((CheckBox) event.getSource());
    }

    void signSelect(CheckBox checkBox) {
        CheckBox[] array = new CheckBox[]{this.signCreating, this.signWaiting, this.signRunning, this.signBlocked, this.signEnded};

        for (int i = 0; i < 5; ++i) {
            array[i].setSelected(checkBox == array[i]);
        }

        System.out.println(checkBox.getId() + "被选中");
        checkBox.setSelected(true);
    }

    @FXML
    void speedSelectByMouse(MouseEvent event) {
        this.speedSelect((RadioButton) event.getSource());
    }

    void speedSelect(RadioButton radioButton) {
        RadioButton[] array = new RadioButton[]{this.speed1Button, this.speed2Button, this.speed4Button, this.speed8Button};

        for (int i = 0; i < 4; ++i) {
            array[i].setSelected(radioButton == array[i]);
        }

        if (this.speed1Button.isSelected()) {
            ProcessManager.speed = 1;
        } else if (this.speed2Button.isSelected()) {
            ProcessManager.speed = 2;
        } else if (this.speed4Button.isSelected()) {
            ProcessManager.speed = 4;
        } else if (this.speed8Button.isSelected()) {
            ProcessManager.speed = 8;
        }

        System.out.println(radioButton.getId() + "被选中");
        radioButton.setSelected(true);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);

        this.showSelect(this.showNow);
        this.signSelect(this.signRunning);
        this.createSelect(this.continueButton);
        this.speedSelect(this.speed1Button);
        this.processName.setCellValueFactory(new PropertyValueFactory<>("processName"));
        this.processState.setCellValueFactory(new PropertyValueFactory<>("processState"));
        this.whichFile.setCellValueFactory(new PropertyValueFactory<>("whichFile"));
        this.havedDevice.setCellValueFactory(new PropertyValueFactory<>("havedDevice"));
        this.havedMemory.setCellValueFactory(new PropertyValueFactory<>("havedMemory"));
        this.havedPid.setCellValueFactory(new PropertyValueFactory<>("havedPid"));
        this.result.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.progressBar.setCellValueFactory(new PropertyValueFactory<>("progressBar"));
        this.instruction.setCellValueFactory(new PropertyValueFactory<>("instruction"));

//        UIThread.processTable = this.processTable;
//        UIThread.nowProcessTable = this.nowProcessTable;
//        UIThread.nowProcessName = this.nowProcessName;
//        UIThread.nowResult = this.nowResult;
//        UIThread.nowInstruction = this.nowInstruction;
//        UIThread.residueSlice = this.residueSlice;
//        UIThread.checkBoxes1 = checkBoxes1;
//        UIThread.checkBoxes2 = checkBoxes2;
         checkBoxes1 = new CheckBox[]{this.showNow, this.showCreating, this.showWaiting, this.showBlocked, this.showEnded, this.showAll};
         checkBoxes2 = new CheckBox[]{this.signCreating, this.signWaiting, this.signRunning, this.signBlocked, this.signEnded};

        ProcessScheduleThread.controlButton = new CheckBox[]{this.continueButton, this.suspendButton};
        DrawUtil drawUtil = new DrawUtil();
        drawUtil.addDrawFunc(stage, this.topMainPane);
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(ProcessAppController.this::adaptWindow));
        stage.heightProperty().addListener((observable, oldValue, newValue)
                -> Platform.runLater(ProcessAppController.this::adaptWindow));

        MainController.getInstance().uiThread.processAppController = this;
    }


    //region [进程管理器的Update方法]
    public void ProcessUpdate()
    {
        this.processTableUpdate();
        this.nowProcessTableUpdate();
        this.nowProcessUpdate();
    }

    private void nowProcessUpdate() {
        if (nowProcessName != null && nowResult != null && nowInstruction != null && residueSlice != null) {
            Platform.runLater(() -> {
                if (MainController.getInstance().uiThread.runProcess == null) {
                    nowProcessName.setText("");
                    nowResult.setText("");
                    nowInstruction.setText("");
                    residueSlice.setText("");
                } else {
                    nowProcessName.setText(MainController.getInstance().uiThread.runProcess.name + "");
                    nowResult.setText(MainController.getInstance().uiThread.runProcess.AX + "");
                    int counter = MainController.getInstance().uiThread.runProcess.PC;
                    if (counter >= MainController.getInstance().uiThread.runProcess.executableFile.instructionArray.size()) {
                        --counter;
                    }

                    nowInstruction.setText(
                            MainController.getInstance().uiThread.runProcess.executableFile.getInstructionArray().get(counter) + "");
                    residueSlice.setText(ProcessScheduleThread.residueSlice + "");
                }

            });
        }
    }

    private void processTableUpdate() {
        if (processTable != null) {
            Vector<?> updateList = null;
            String selectString = "";
            if (checkBoxes1[0].isSelected()) {
                selectString = checkBoxes1[0].getText();
                updateList = (Vector<?>) ProcessManager.allProcessList.clone();
            } else if (checkBoxes1[1].isSelected()) {
                selectString = checkBoxes1[1].getText();
                updateList = (Vector<?>) ProcessManager.creatingProcessList.clone();
            } else if (checkBoxes1[2].isSelected()) {
                selectString = checkBoxes1[2].getText();
                updateList = (Vector<?>) ProcessManager.waitProcessList.clone();
            } else if (checkBoxes1[3].isSelected()) {
                selectString = checkBoxes1[3].getText();
                updateList = (Vector<?>) ProcessManager.blockProcessList.clone();
            } else if (checkBoxes1[4].isSelected()) {
                selectString = checkBoxes1[4].getText();
                updateList = (Vector<?>) ProcessManager.allProcessList.clone();
            } else if (checkBoxes1[5].isSelected()) {
                selectString = checkBoxes1[5].getText();
                updateList = (Vector<?>) ProcessManager.allProcessList.clone();
            }

            DataLoader.processDetailDataLoad(UIThread.processDetailDataArrayList, (Vector<Process>) updateList, selectString);
            Platform.runLater(() -> processTable.setItems(FXCollections.observableArrayList(UIThread.processDetailDataArrayList)));
            processTable.setRowFactory((row) -> new TableRow<>() {
                public void updateItem(ProcessDetailData item, boolean empty) {
                    super.updateItem(item, empty);
                    String s = "运行态";
                    if (checkBoxes2 != null) {
                        for (int i = 0; i < checkBoxes2.length; ++i) {
                            if (checkBoxes2[i].isSelected()) {
                                if (i == 0) {
                                    s = "新建态";
                                } else if (i == 1) {
                                    s = "就绪态";
                                } else if (i == 2) {
                                    s = "运行态";
                                } else if (i == 3) {
                                    s = "阻塞态";
                                } else if (i == 4) {
                                    s = "已销毁";
                                }
                                break;
                            }
                        }
                    }

                    if (item == null) {
                        this.setId("not-right");
                    } else if (Objects.equals(item.getProcessState(), s)) {
                        this.setId("right");
                    } else {
                        this.setId("not-right");
                    }

                }
            });
        }
    }

    private void nowProcessTableUpdate() {
        if (nowProcessTable != null) {
            if (MainController.getInstance().uiThread.runProcess != null) {
                DataLoader.fileDetailDataLoad(UIThread.instructionDataArrayList, MainController.getInstance().uiThread.runProcess.executableFile);
                nowProcessTable.setRowFactory((row) -> new TableRow<>() {
                    public void updateItem(InstructionData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            this.setId("not-right");
                        } else if (item.which == MainController.getInstance().uiThread.runProcess.PC) {
                            this.setId("right");
                        } else {
                            this.setId("not-right");
                        }

                    }
                });
            } else {
                UIThread. instructionDataArrayList.clear();
            }

            Platform.runLater(() -> nowProcessTable.setItems(FXCollections.observableArrayList(UIThread.instructionDataArrayList)));
        }
    }

    //endregion
}
