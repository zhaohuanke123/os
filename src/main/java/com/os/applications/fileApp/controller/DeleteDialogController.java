package com.os.applications.fileApp.controller;

import com.os.applications.fileApp.application.FileApplication;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.Folder;
import com.os.utility.fileSystem.Path;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;

public class DeleteDialogController extends BaseFileController {
    @FXML
    private Button acceptButton;  // 确定按钮
    @FXML
    private Button cancelButton;  // 取消按钮
    @FXML
    private Text text;  // 显示文本
    private Disk block;  // 要删除的磁盘块

    public void init(final Stage stage, final FileApplication mainView, String tipString, final Disk block) {
        super.init(stage);
        this.text.setText(tipString);
        this.block = block;

        // 当点击确定按钮时触发的事件
        this.acceptButton.setOnMouseClicked(event -> {
            this.closeStage();
            Path thisPath = null;
            if (block.getObject() instanceof Folder) {
                thisPath = ((Folder) block.getObject()).getPath();
            }

            // 调用 FileApp 中的删除方法
            int res = FileApplication.fat.delete(block);
            // 删除文件夹成功，移除节点
            if (res == 0) mainView.removeNode(mainView.getCurrentNode(), thisPath);
                // 文件夹不为空
            else if (res == 2) {
                try {
                    FileApplication.tipOpen("文件夹不为空!");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
            // 文件未关闭
            else if (res == 3) {
                try {
                    FileApplication.tipOpen("文件未关闭!");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }

            // 清空图标区域并显示更新后的图标
            mainView.controller.flowPane.getChildren().removeAll(mainView.controller.flowPane.getChildren());
            mainView.addIcon(FileApplication.fat.getBlockList(mainView.currentPath));
        });

        // 当点击取消按钮时触发的事件
        this.cancelButton.setOnMouseClicked(event -> this.closeStage());
    }

    public void init(final Stage stage, FileApplication mainView, String tipString) {
        super.init(stage);

        this.cancelButton.setOnMouseClicked(event -> this.closeStage());
        this.acceptButton.setOnMouseClicked(event -> {
            this.closeStage();
            mainView.clearData();
        });
        text.setText(tipString);
        text.setFont(javafx.scene.text.Font.font(20));
        text.setFill(Color.RED);
    }
}
