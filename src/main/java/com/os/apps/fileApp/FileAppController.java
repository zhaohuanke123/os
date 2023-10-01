package com.os.apps.fileApp;

import com.os.apps.fileApp.Controller.BaseFileCtl;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.File;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FileAppController extends BaseFileCtl {
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
        FileApp.saveData();
        this.stage.close();
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);
    }
}