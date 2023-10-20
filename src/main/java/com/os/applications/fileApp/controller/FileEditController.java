package com.os.applications.fileApp.controller;


import com.os.applications.fileApp.application.FileEditApplication;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.FAT;
import com.os.utility.fileSystem.File;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FileEditController extends BaseFileController {
//    public MenuBar menuBar;
//    public Menu fileMenu;
    public MenuItem saveItem;
    public MenuItem save_close;
    public MenuItem closeItem;
    public TextArea contentField;
    Disk block;
    File file;

    public void init(File file, Stage stage, Disk block) {
        super.init(stage);
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
