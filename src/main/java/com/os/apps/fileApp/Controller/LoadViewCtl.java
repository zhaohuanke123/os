package com.os.apps.fileApp.Controller;

import com.os.apps.BaseController;
import com.os.apps.fileApp.app.MainUI;
import com.os.apps.fileApp.app.TipWindow;
import com.os.utils.fileSystem.FAT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class LoadViewCtl extends BaseController {
    public Button no;
    public Button yes;

    @FXML
    protected void closeStage(MouseEvent event) {
        File log = new File("./data");
        FileWriter fileWriter;

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
