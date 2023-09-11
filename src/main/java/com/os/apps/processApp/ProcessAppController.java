package com.os.apps.processApp;

import com.os.apps.BaseController;
import com.os.datas.InstructionData;
import com.os.datas.ProcessDetailData;
import com.os.utils.ui.DrawUtil;
import com.os.utils.process.ProcessManager;
import com.os.utils.process.ProcessScheduleThread;
import com.os.utils.ui.UIThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ProcessAppController extends BaseController {
    public AnchorPane mainPane;
    public Label titleBarL;
    public HBox titleBarR;
    @FXML
    private AnchorPane topMainPane;
    @FXML
    private TableView<?> processTable;
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
    private TableView<?> nowProcessTable;
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
        UIThread.processTable = (TableView<ProcessDetailData>) this.processTable;
        UIThread.nowProcessTable = (TableView<InstructionData>) this.nowProcessTable;
        UIThread.nowProcessName = this.nowProcessName;
        UIThread.nowResult = this.nowResult;
        UIThread.nowInstruction = this.nowInstruction;
        UIThread.residueSlice = this.residueSlice;
        UIThread.checkBoxes1 = new CheckBox[]{this.showNow, this.showCreating, this.showWaiting, this.showBlocked, this.showEnded, this.showAll};
        UIThread.checkBoxes2 = new CheckBox[]{this.signCreating, this.signWaiting, this.signRunning, this.signBlocked, this.signEnded};
        ProcessScheduleThread.controlButton = new CheckBox[]{this.continueButton, this.suspendButton};
        DrawUtil drawUtil = new DrawUtil();
        drawUtil.addDrawFunc(stage, this.topMainPane);
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(ProcessAppController.this::adaptWindow));
        stage.heightProperty().addListener((observable, oldValue, newValue)
                -> Platform.runLater(ProcessAppController.this::adaptWindow));
    }
}
