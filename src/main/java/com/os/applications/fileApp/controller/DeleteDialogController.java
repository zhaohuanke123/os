package com.os.applications.fileApp.controller;

import com.os.applications.fileApp.application.FileApplication;
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
                    FileApplication.tipOpen("文件夹不为空");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            } else {
                try {
                    FileApplication.tipOpen("文件未关闭");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }

            mainView.controller.flowPane.getChildren().removeAll(mainView.controller.flowPane.getChildren());

            mainView.addIcon(FileApplication.fat.getBlockList(mainView.recentPath), mainView.recentPath);
        });

        this.cancelButton.setOnMouseClicked(event -> this.closeStage());
    }

//    public void tipOpen(String tipString) throws Exception {
//        Stage stage = new Stage();
//        TipDialogApplication tipWindow = new TipDialogApplication(tipString);
//        tipWindow.start(stage);
//        Text text = new Text(tipString);
//        text.setFill(javafx.scene.paint.Color.RED);
//        text.setFont(javafx.scene.text.Font.font("宋体", 25.0D));
//        tipWindow.controller.tipTextFlow.getChildren().add(text);
//    }
}
