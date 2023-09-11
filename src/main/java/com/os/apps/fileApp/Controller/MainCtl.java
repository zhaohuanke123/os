package com.os.apps.fileApp.Controller;

import com.os.apps.fileApp.app.MainUI;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainCtl extends BaseFileController{
    public HBox titleBar;
    @FXML
    private HBox toolBar;
    @FXML
    private TabPane TabP;
    @FXML
    private Label currentPath;
    @FXML
    private TreeView<?> treeView;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Tab chartTab;
    @FXML
    private PieChart pie;
    @FXML
    private TableView<?> diskTable;
    @FXML
    private TableView<?> openFile;
    @FXML
    private TableColumn<?, ?> filePath;

    public void init(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void closeStage(MouseEvent event) {
        MainUI.saveData();
        this.stage.close();
    }
}
