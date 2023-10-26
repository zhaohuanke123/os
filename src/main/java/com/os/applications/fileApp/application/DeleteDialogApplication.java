package com.os.applications.fileApp.application;

import com.os.applications.fileApp.controller.DeleteDialogController;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.File;
import com.os.utility.fileSystem.Folder;

import java.io.IOException;

import javafx.stage.Stage;

public class DeleteDialogApplication extends BaseFileApplication<DeleteDialogController> {
    private final Disk block;
    private final String tipString;
    private final FileApplication mainView;

    public DeleteDialogApplication(FileApplication mainView, Disk block) {
        super(
                "/com/os/applications/fileApp/fxmls/DeleteDialog.fxml",
                "/com/os/applications/fileApp/res/tip.png",
                "删除",
                -1,
                -1
        );

        this.mainView = mainView;
        this.block = block;
        String msg;
        if (block.getObject() instanceof Folder) {
            Folder folder = (Folder) block.getObject();
            msg = "名称: " + folder.getName() + "\n类型: " + folder.getType() + "\n大小: " + folder.getSize() + "B\n创建时间: " + folder.getCreateTime();
        } else {
            File file = (File) block.getObject();
            msg = "名称: " + file.getName() + "\n类型: " + file.getType() + "\n大小: " + file.getSize() + "B\n创建时间: " + file.getCreateTime();
        }

        this.tipString = msg;
        super.toolTip = msg;
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        controller.init(stage, this.mainView, this.tipString, this.block);
    }
}
