package com.os.apps.fileApp.Controller;


import com.os.apps.BaseController;
import com.os.apps.fileApp.app.FileView;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.FAT;
import com.os.utils.fileSystem.File;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FileViewCtl extends BaseController {
    public BorderPane titleBar;
    public Label title;
    public MenuBar menuBar;
    public Menu fileMenu;
    public MenuItem saveItem;
    public MenuItem save_close;
    public MenuItem closeItem;
    public TextArea contentField;
    public HBox titleBarL;
    Disk block;
    File file;

    public void init(File file, Stage stage, Disk block) {
        super.init(stage);
        this.file = file;
        this.block = block;
    }

    @FXML
    protected void closeStage(MouseEvent event) {
        FileView.maps.remove(this.file);
        FAT.removeOpenedFile(this.block);
        this.stage.close();
    }
}
