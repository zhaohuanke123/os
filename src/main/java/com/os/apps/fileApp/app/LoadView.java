package com.os.apps.fileApp.app;

import com.os.apps.fileApp.Controller.LoadViewCtl;
import com.os.utils.fileSystem.FAT;
import com.os.utils.fileSystem.File;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.os.main.MainController;
import com.os.utils.ui.StageRecord;

public class LoadView {
    LoadView() throws IOException {
        Stage stage = MainController.checkStage("load");
        if (stage != null && !stage.isShowing()) {
            MainController.removeStage("load");
        }

        stage = MainController.checkStage("load");
        FXMLLoader fileLoader;
        if (stage == null) {
            URL location = this.getClass().getResource("/com/os/apps/fileApp/fxmls/loadView.fxml");
            if (location == null) {
                System.out.println("null");
                return;
            }

            fileLoader = new FXMLLoader(this.getClass().getResource("/com/os/apps/fileApp/fxmls/loadView.fxml"));
            Parent root1 = fileLoader.load();
            stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setIconified(false);
            stage.toFront();
            Scene scene = new Scene(root1, 300.0, 300.0);
            stage.setScene(scene);
            MainController.stageList.add(new StageRecord("load", stage));
            MainController.updateStageList("load");
            location = this.getClass().getResource("/com/os/apps/fileApp/res/tip.png");
            stage.getIcons().add(new Image(String.valueOf(location)));
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            LoadViewCtl loadViewCtl = fileLoader.getController();
            loadViewCtl.init(stage);
            stage.setResizable(false);
            stage.toFront();
            stage.showAndWait();
        } else if (stage.isShowing()) {
            stage.toFront();
            stage.showAndWait();
        }

        if (MainUI.clearFlag) {
            File log = new File("./data");

            try {
                FileWriter fileWriter = new FileWriter(String.valueOf(log));
                fileWriter.write("");
                System.out.println("clear");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException var7) {
                System.out.println(var7.getMessage());
            }

            MainUI.fat = new FAT();
            System.out.println("clear1");
        }

    }
}
