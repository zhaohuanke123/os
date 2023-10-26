package com.os.applications.fileApp.controller;

import com.os.applications.fileApp.application.FileApplication;
import com.os.applications.fileApp.application.TipDialogApplication;
import com.os.applications.processControlApp.processSystem.ExeFile;
import com.os.applications.processControlApp.processSystem.ProcessManager;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class FileApplicationController extends BaseFileController {
    public TabPane TabP;
    public Label currentPath;
    public TreeView<String> treeView;
    public FlowPane flowPane;
    @FXML
    public TableView<Disk> diskTable;
    @FXML
    public TableView<File> openFile;
    public TextArea contentField;

    @FXML
    public void closeStage() {
        FileApplication.saveData();
        this.stage.close();
    }

    @FXML
    void selectFile() {
        String s = this.executableFileTable.getSelectionModel().getSelectedItem().toString();
        int i = Integer.parseInt(s);
        this.updateContentField(i);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);

        exeFileList = ProcessManager.exeFileList;
        this.fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        updateFileTable(this.executableFileTable);
    }


    @FXML
    private TableView<ExecutableFileData> executableFileTable;
    @FXML
    private TableColumn<?, ?> fileName;

    public static Vector<ExeFile> exeFileList;
    public static ArrayList<ExecutableFileData> executableFileDataList = new ArrayList<>();
    public ArrayList<InstructionData> fileDetailDataList = new ArrayList<>();

    public static void updateFileTable(final TableView<ExecutableFileData> executableFileTable) {
        Platform.runLater(() -> {
            DataLoader.fileDataLoad(
                    FileApplicationController.executableFileDataList,
                    FileApplicationController.exeFileList);
            executableFileTable.setItems(
                    FXCollections.observableArrayList(
                            FileApplicationController.executableFileDataList));
        });
    }

    public void updateContentField(final int i) {
        Platform.runLater(() -> {
            DataLoader.fileDetailDataLoad(
                    FileApplicationController.this.fileDetailDataList,
                    FileApplicationController.exeFileList.get(i));
            StringBuilder stringBuilder = new StringBuilder();
            for (InstructionData instructionData : FileApplicationController.this.fileDetailDataList) {
                stringBuilder.append(instructionData.getInstruction()).append("\n");
            }
            FileApplicationController.this.contentField.setText(stringBuilder.toString());
        });
    }

    @FXML
    @Override
    protected void showDescription() {
        super.showDescription();

        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication("",500,500);
        try {
            tipWindow.start(stage);
            Text text = new Text("文件管理器\n\n");
            text.setFill(Color.RED);
            text.setFont(Font.font("宋体", 25));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("1. 文件管理页面。在该页面可对文件进行新建、读写、移动、复制、删除、属性查看操作，对文件夹进行新建、删除、移动、复制、属性查看操作。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("2. 磁盘使用页面。在该页面显示了磁盘的占用盘块、索引、类型、内容信息。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("3. 已打开文件页面，在该页面显示了当前已打开文件的文件名、打开方式、起始盘块、文件长度、文件路径信息。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);

            text = new Text("4. 系统可执行文件页面，在该页面显示了可执行文件的具体内容，10个可执行文件的内容均为随机生成。\n\n");
            text.setFill(Color.BLACK);
            text.setFont(Font.font("宋体", 20));
            tipWindow.controller.tipTextFlow.getChildren().add(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}