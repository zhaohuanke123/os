package com.os.apps.fileApp.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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

public class LoadViewCtl extends BaseFileController {
    public HBox titleBar;
    @FXML
    private Button yes;
    @FXML
    private Button no;

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
            System.out.println(Arrays.toString(var5.getStackTrace()));
        }

        new FAT();
        this.stage.close();
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
