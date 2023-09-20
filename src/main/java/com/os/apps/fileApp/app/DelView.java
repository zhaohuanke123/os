package com.os.apps.fileApp.app;

import com.os.apps.BaseApp;
import com.os.apps.fileApp.Controller.DelViewCtl;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.File;
import com.os.utils.fileSystem.Folder;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DelView extends BaseApp<DelViewCtl> {
    private final Disk block;
    private final String tipString;
    private final MainUI mainView;

    public DelView(MainUI mainView, Disk block) {
        super(
                "/com/os/apps/fileApp/fxmls/delView.fxml",
                "/com/os/apps/fileApp/res/tip.png",
                "删除",
                -1,
                -1
        );

        this.mainView = mainView;
        this.block = block;
        String msg;
        if (block.getObject() instanceof Folder) {
            Folder folder = (Folder) block.getObject();
            msg = "名称: " + folder.getFolderName() + "\n类型: " + folder.getType() + "\n大小: " + folder.getSize() + "B\n创建时间: " + folder.getCreateTime();
        } else {
            File file = (File) block.getObject();
            msg = "名称: " + file.getFileName() + "\n类型: " + file.getType() + "\n大小: " + file.getSize() + "B\n创建时间: " + file.getCreateTime();
        }

        this.tipString = msg;
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        controller.init(stage, this.mainView, this.tipString, this.block);
    }
}
