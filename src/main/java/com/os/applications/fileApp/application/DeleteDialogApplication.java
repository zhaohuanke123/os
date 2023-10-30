package com.os.applications.fileApp.application;

import com.os.applications.fileApp.controller.DeleteDialogController;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.File;
import com.os.utility.fileSystem.Folder;

import java.io.IOException;

import javafx.stage.Stage;

public class DeleteDialogApplication extends BaseFileApplication<DeleteDialogController> {
    private Disk block;  // 要删除的磁盘块
    private String tipString;  // 用于显示的提示信息
    private FileApplication mainView;  // 主视图

    public DeleteDialogApplication() {
        super(
                "/com/os/applications/fileApp/fxmls/DeleteDialog.fxml",
                "/com/os/applications/fileApp/res/tip.png",
                "删除",
                -1,
                -1
        );
        super.toolTip = "提示";
    }

    public DeleteDialogApplication(FileApplication mainView, Disk block) {
        super(
                "/com/os/applications/fileApp/fxmls/DeleteDialog.fxml",
                "/com/os/applications/fileApp/res/tip.png",
                "删除",
                -1,
                -1
        );

        String msg;
        if (block.getObject() instanceof Folder) {
            Folder folder = (Folder) block.getObject();
            msg = "名称: " + folder.getName() +
                    "\n类型: " + folder.getType() +
                    "\n大小: " + folder.getSize() + "B" +
                    "\n创建时间: " + folder.getCreateTime();
        } else {
            File file = (File) block.getObject();
            msg = "名称: " + file.getName() +
                    "\n类型: " + file.getType() +
                    "\n大小: " + file.getSize() + "B" +
                    "\n创建时间: " + file.getCreateTime();
        }

        this.mainView = mainView;
        this.block = block;
        this.tipString = msg;
        super.toolTip = "提示";
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);
        controller.init(stage, this.mainView, this.tipString, this.block);
    }

    public void start(Stage stage, FileApplication mainView) throws IOException {
        super.start(stage);
        controller.init(stage, mainView, "是否选择格式化磁盘？");
        controller.title.setText("格式化磁盘");
    }
}
