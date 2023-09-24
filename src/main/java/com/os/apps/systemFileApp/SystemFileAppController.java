package com.os.apps.systemFileApp;

import com.os.apps.BaseController;
import com.os.datas.ExecutableFileData;
import com.os.datas.InstructionData;
import com.os.utils.DataLoader;
import com.os.utils.ui.DrawUtil;
import com.os.utils.process.ExecutableFile;
import com.os.utils.process.ProcessManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Vector;

public class SystemFileAppController extends BaseController {
    @FXML
    private TableView<ExecutableFileData> executableFileTable;
    @FXML
    private TableColumn<?, ?> fileName;
    @FXML
    private TableView<InstructionData> fileDetailTable;
    @FXML
    private TableColumn<?, ?> fileDetail;
    public static Vector<ExecutableFile> executableFileList;
    public static ArrayList<ExecutableFileData> executableFileDataList = new ArrayList<>();
    public ArrayList<InstructionData> fileDetailDataList = new ArrayList<>();

    @FXML
    void selectFile(MouseEvent event) {
        String s = this.executableFileTable.getSelectionModel().getSelectedItem().toString();
        int i = Integer.parseInt(s);
        this.updateFileDetailTable(i);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);

        executableFileList = ProcessManager.executableFileList;
        this.fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        this.fileDetail.setCellValueFactory(new PropertyValueFactory<>("instruction"));
        updateFileTable(this.executableFileTable);
    }

    public static void updateFileTable(final TableView<ExecutableFileData> executableFileTable) {
        Platform.runLater(() -> {
            DataLoader.fileDataLoad(
                    SystemFileAppController.executableFileDataList,
                    SystemFileAppController.executableFileList);
            executableFileTable.setItems(
                    FXCollections.observableArrayList(
                            SystemFileAppController.executableFileDataList));
        });
    }

    public void updateFileDetailTable(final int i) {
        Platform.runLater(() -> {
            DataLoader.fileDetailDataLoad(
                    SystemFileAppController.this.fileDetailDataList,
                    SystemFileAppController.executableFileList.get(i));
            SystemFileAppController.this.fileDetail.setText("对应编号：" + i);
            SystemFileAppController.this.fileDetailTable.setItems(
                    FXCollections.observableArrayList(SystemFileAppController.this.fileDetailDataList));
        });
    }
}
