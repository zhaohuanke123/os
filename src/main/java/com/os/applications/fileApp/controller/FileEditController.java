package com.os.applications.fileApp.controller;


import com.os.applications.fileApp.application.FileEditApplication;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.FAT;
import com.os.utility.fileSystem.File;
import com.os.utility.uiUtil.DrawUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FileEditController extends BaseFileController {
    public MenuItem saveItem;
    public MenuItem save_close;
    public MenuItem closeItem;
    public TextArea contentField;
    Disk block;
    File file;

    public void init(File file, Stage stage, Disk block) {
        super.init(stage);

        // 创建绘图工具
        DrawUtil drawUtil = new DrawUtil();
        drawUtil.addDrawFunc(stage, this.topMainPane);

        // 监听窗口大小变化，根据窗口大小自适应布局
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
        this.adaptWindow();  // 初始时适应窗口

        this.file = file;
        this.block = block;
    }

    @FXML
    @Override
    public void closeStage() {
        super.closeStage();

        FileEditApplication.maps.remove(this.file);
        FAT.removeOpenedFile(this.block);
    }
}
