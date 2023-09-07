package com.os.apps.fileApp.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.os.apps.fileApp.app.MainUI;
import com.os.apps.fileApp.app.TipWindow;
import com.os.utils.fileSystem.FAT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoadViewCtl {
    public HBox titleBar;
    @FXML
    private Button yes;
    @FXML
    private Button no;
    double xOffset = 0.0;
    double yOffset = 0.0;
    Stage stage;

    @FXML
    void closeStage(MouseEvent event) {
        File log = new File("./data");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(String.valueOf(log));
            fileWriter.write("");
            System.out.println("clear");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        new FAT();
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

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipWindow tipWindow = new TipWindow(tipString);
        tipWindow.start(stage);
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
    }

    public void yesBtn(ActionEvent actionEvent) {
        this.stage.close();
    }

    public void noBtn(ActionEvent actionEvent) {
        MainUI.clearFlag = true;
        this.stage.close();
    }
}
