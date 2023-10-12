package com.os.applications.fileApp;

import com.os.applications.fileApp.application.TipDialogApplication;
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

import java.io.IOException;
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

    @FXML
    @Override
    protected void showDescription() {
        super.showDescription();

        Stage stage = new Stage();
//        TipDialogApplication tipWindow = new TipDialogApplication("系统文件表，其主要作用是显示系统可执行文件和对应的可执行文件内容。左侧表格是可执行文件表，右侧表格是可执行文件的内容。点击左侧表格任意一个文件，相应的指令内容则会在右侧表格显示。&#10;\n" +
//                "系统开启时，会随机生成10个可执行文件，用于后续的进程创建、内存分配、进程执行/调度和设备分配。\n" +
//                "可执行文件包含五种指令：\n" +
//                "1）x=？：给x赋值。\n" +
//                "2）x++ ：x加1。\n" +
//                "3）x - - ：x减1。\n" +
//                "4）! ? ? ：！是指令前缀；第一个？为A、B、C中的某个设备；第一个？为设备使用的时间。\n" +
//                "5）end ：表示程序结束。\n" +
//                " 其他说明：可执行文件中x的初值默认为0。",
//                500,500);
        TipDialogApplication tipWindow = new TipDialogApplication("文件管理，其主要作用是对文件进行操作，以及显示磁盘使用情况。" +
                "1）当启动该软件时，会询问是否读取历史数据，若是，则进行读取；若否，则清空之前的数据。" +
                "2）第一个模块是文件管理页面。在该页面，通过鼠标及键盘操作，可进行对文件的新建、读写、移动、复制、删除、属性查看等操作，可进行对文件夹的新建、删除、移动、复制、属性查看等操作。" +
                "3）第二个模块是磁盘的使用情况页面。在该页面，显示了磁盘的占用情况：①占用分布图；②占用盘块、索引、类型、内容等信息；③当前打开的文件对应文件名、打开方式、起始盘块、文件长度、文件路径等信息。",
                500,500);
        try {
            tipWindow.start(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}