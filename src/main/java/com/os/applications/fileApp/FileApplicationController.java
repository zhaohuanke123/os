package com.os.applications.fileApp;

import com.os.applications.fileApp.controller.BaseFileController;
import com.os.applications.processApp.processSystem.ExecutableFile;
import com.os.applications.processApp.processSystem.ProcessManager;
import com.os.dataModels.ExecutableFileData;
import com.os.dataModels.InstructionData;
import com.os.utility.DataLoader;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.File;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Vector;

public class FileApplicationController extends BaseFileController {
    public TabPane TabP;
    public Label currentPath;
    public TreeView<String> treeView;
    public FlowPane flowPane;
    public Tab chartTab;
    @FXML
    public TableView<Disk> diskTable;
    @FXML
    public TableView<File> openFile;
    public Tab systemFileTab;
    public TextArea contentField;
    @FXML
    private TableColumn<?, ?> filePath;

    @FXML
    public void closeStage(MouseEvent event) {
        FileApplication.saveData();
        this.stage.close();
    }

    @FXML
    void selectFile(MouseEvent event) {
        String s = this.executableFileTable.getSelectionModel().getSelectedItem().toString();
        int i = Integer.parseInt(s);
//        this.updateFileDetailTable(i);
        this.updateContentField(i);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);

        executableFileList = ProcessManager.executableFileList;
        this.fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        updateFileTable(this.executableFileTable);
    }


    @FXML
    private TableView<ExecutableFileData> executableFileTable;
    @FXML
    private TableColumn<?, ?> fileName;

    public static Vector<ExecutableFile> executableFileList;
    public static ArrayList<ExecutableFileData> executableFileDataList = new ArrayList<>();
    public ArrayList<InstructionData> fileDetailDataList = new ArrayList<>();

    public static void updateFileTable(final TableView<ExecutableFileData> executableFileTable) {
        Platform.runLater(() -> {
            DataLoader.fileDataLoad(
                    FileApplicationController.executableFileDataList,
                    FileApplicationController.executableFileList);
            executableFileTable.setItems(
                    FXCollections.observableArrayList(
                            FileApplicationController.executableFileDataList));
        });
    }

    public void updateContentField(final int i) {
        Platform.runLater(() -> {
            DataLoader.fileDetailDataLoad(
                    FileApplicationController.this.fileDetailDataList,
                    FileApplicationController.executableFileList.get(i));
            StringBuilder stringBuilder = new StringBuilder();
            for (InstructionData instructionData : FileApplicationController.this.fileDetailDataList) {
                stringBuilder.append(instructionData.getInstruction()).append("\n");
            }
            FileApplicationController.this.contentField.setText(stringBuilder.toString());
        });
    }
}