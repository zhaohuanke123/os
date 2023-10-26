package com.os.applications.fileApp.controller;

import com.os.applications.fileApp.application.FileApplication;
import com.os.applications.fileApp.application.TipDialogApplication;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class FileApplicationController extends BaseFileController {
    public TabPane TabP;  // 选项卡面板
    public Label currentPath;  // 显示当前路径的标签
    public TreeView<String> treeView;  // 文件树视图
    public FlowPane flowPane;  // 文件和文件夹图标的容器
    public TextArea contentField;  // 显示文件内容的文本区域
    public static Vector<ExecutableFile> executableFileList;  // 可执行文件列表
    public static ArrayList<ExecutableFileData> executableFileDataList = new ArrayList<>();  // 可执行文件数据列表
    public ArrayList<InstructionData> fileDetailDataList = new ArrayList<>();  // 文件详情数据列表

    @FXML
    public TableView<Disk> diskTable;  // 显示磁盘信息的表格
    @FXML
    public TableView<File> openFile;  // 显示已打开文件信息的表格
    @FXML
    private TableView<ExecutableFileData> executableFileTable;  // 显示可执行文件数据的表格
    @FXML
    private TableColumn<?, ?> fileName;  // 文件名列


    // 关闭窗口事件处理
    @FXML
    public void closeStage() {
        FileApplication.saveData();
        this.stage.close();
    }

    // 选择文件事件处理
    @FXML
    void selectFile() {
        String s = this.executableFileTable.getSelectionModel().getSelectedItem().toString();
        int i = Integer.parseInt(s);
        // 更新文件内容区域
        this.updateContentField(i);
    }

    // 初始化 FileApp 界面
    @Override
    public void init(Stage stage) {
        super.init(stage);
        // 从进程管理器获取可执行文件列表
        executableFileList = ProcessManager.executableFileList;
        // 设置文件名列
        this.fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        // 更新可执行文件表
        updateFileTable(this.executableFileTable);
    }

    // 更新可执行文件表
    public static void updateFileTable(final TableView<ExecutableFileData> executableFileTable) {
        Platform.runLater(() -> {
            // 加载可执行文件数据
            DataLoader.fileDataLoad(
                    FileApplicationController.executableFileDataList,
                    FileApplicationController.executableFileList);
            // 设置为executableFileTable的数据
            executableFileTable.setItems(FXCollections.observableArrayList(FileApplicationController.executableFileDataList));
        });
    }

    // 更新文件内容区域
    public void updateContentField(final int i) {
        Platform.runLater(() -> {
            // 加载文件详细数据
            DataLoader.fileDetailDataLoad(
                    FileApplicationController.this.fileDetailDataList,
                    FileApplicationController.executableFileList.get(i));
            StringBuilder stringBuilder = new StringBuilder();
            // 遍历文件详细数据列表，获取指令并构建文本内容
            for (InstructionData instructionData : FileApplicationController.this.fileDetailDataList)
                stringBuilder.append(instructionData.getInstruction()).append("\n");
            // 将文本内容设置为contentField的文本
            FileApplicationController.this.contentField.setText(stringBuilder.toString());
        });
    }

    // 文件帮助界面的信息
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