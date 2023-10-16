package com.os.applications.fileApp.controller;

import com.os.applications.fileApp.FileApplication;
import com.os.applications.fileApp.application.TipDialogApplication;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.Folder;
import com.os.utility.fileSystem.Path;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;

public class DeleteDialogController extends BaseFileController {
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text text;
    private Disk block;

    public void init(final Stage stage, final FileApplication mainView, String tipString, final Disk block) {
        super.init(stage);
        this.text.setText(tipString);
        this.block = block;

        this.acceptButton.setOnMouseClicked(event -> {
            this.closeStage();
            Path thisPath = null;
            if (block.getObject() instanceof Folder) {
                thisPath = ((Folder) block.getObject()).getPath();
            }

            int res = FileApplication.fat.delete(block);
            if (res == 0) {
                mainView.removeNode(mainView.getRecentNode(), thisPath);
            } else if (res == 1) {
            } else if (res == 2) {
                try {
                    DeleteDialogController.this.tipOpen("文件夹不为空");
                } catch (Exception var6) {
                    System.out.println(Arrays.toString(var6.getStackTrace()));
                }
            } else {
                try {
                    DeleteDialogController.this.tipOpen("文件未关闭");
                } catch (Exception var5) {
                    System.out.println(Arrays.toString(var5.getStackTrace()));
                }
            }

            mainView.controller.flowPane.getChildren().removeAll(mainView.controller.flowPane.getChildren());

            mainView.addIcon(FileApplication.fat.getBlockList(mainView.recentPath), mainView.recentPath);
        });

        this.cancelButton.setOnMouseClicked(event -> this.closeStage());
    }

    public void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication(tipString);
        tipWindow.start(stage);
    }
}
