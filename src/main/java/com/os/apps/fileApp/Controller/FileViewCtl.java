package com.os.apps.fileApp.Controller;


import com.os.apps.fileApp.app.FileView;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.FAT;
import com.os.utils.fileSystem.File;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FileViewCtl {
    public HBox titleBar;
    public Label fileIcon;
    public MenuBar menuBar;
    public Menu fileMenu;
    public MenuItem saveItem;
    public MenuItem save_close;
    public MenuItem closeItem;
    public TextArea contentField;
    Stage stage;
    Disk block;
    File file;
    double xOffset;
    double yOffset;

    public void init(File file, Stage stage, Disk block) {
        this.file = file;
        this.stage = stage;
        this.block = block;
    }

    @FXML
    void closeStage(MouseEvent event) {
        FileView.maps.remove(this.file);
        FAT.removeOpenedFile(this.block);
        this.stage.close();
    }

    @FXML
    void minimizeStage(MouseEvent event) {
        this.stage.setIconified(true);
    }

    @FXML
    void pressBar(MouseEvent event) {
        this.xOffset = event.getSceneX();
        this.yOffset = event.getSceneY();
    }

    @FXML
    void dragBar(MouseEvent event) {
        this.stage.setX(event.getScreenX() - this.xOffset);
        this.stage.setY(event.getScreenY() - this.yOffset);
//        this.stage.setOpacity(0.800000011920929);
    }

    @FXML
    void dragBarDone(DragEvent event) {
        this.stage.setOpacity(1.0);
    }

    @FXML
    void releaseBar(MouseEvent event) {
        this.stage.setOpacity(1.0);
    }
}
