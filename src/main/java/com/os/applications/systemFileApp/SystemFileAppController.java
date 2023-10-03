package com.os.applications.systemFileApp;

import com.os.applications.BaseController;
import com.os.dataModels.ExecutableFileData;
import com.os.dataModels.InstructionData;
import com.os.utility.DataLoader;
import com.os.applications.processApp.processSystem.ExecutableFile;
import com.os.applications.processApp.processSystem.ProcessManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
