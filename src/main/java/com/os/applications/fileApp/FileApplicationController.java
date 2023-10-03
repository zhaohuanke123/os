package com.os.applications.fileApp;

import com.os.applications.fileApp.controller.BaseFileController;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.File;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

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
    @FXML
    private TableColumn<?, ?> filePath;

    @FXML
    protected void closeStage(MouseEvent event) {
        FileApplication.saveData();
        this.stage.close();
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);
    }
}