package com.os.apps.fileApp.Controller;

import com.os.apps.BaseController;
import com.os.apps.fileApp.app.MainUI;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.File;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainCtl extends BaseController {
    @FXML
    private HBox toolBar;
    public TabPane TabP;
    public Label currentPath;
    public TreeView<String> treeView;
    public FlowPane flowPane;
    public Tab chartTab;
    @FXML
    public PieChart pieChart;
    @FXML
    public TableView<Disk> diskTable;
    @FXML
    public TableView<File> openFile;
    @FXML
    private TableColumn<?, ?> filePath;

    @FXML
    protected void closeStage(MouseEvent event) {
        MainUI.saveData();
        this.stage.close();
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);

    }
}