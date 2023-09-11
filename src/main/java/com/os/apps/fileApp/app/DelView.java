package com.os.apps.fileApp.app;

import com.os.apps.fileApp.Controller.DelViewCtl;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.File;
import com.os.utils.fileSystem.Folder;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DelView extends Application {
    private final Disk block;
    private final String tipString;
    private final MainUI mainView;

    public DelView(MainUI mainView, Disk block) {
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

    public void start(Stage primaryStage) throws Exception {
        URL location = this.getClass().getResource("/com/os/apps/fileApp/fxmls/delView.fxml");
        if (location == null) {
            System.out.println("null");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("删除");
            Scene MainScene = new Scene(root);
            primaryStage.setScene(MainScene);
            Scene scene = primaryStage.getScene();
            DelViewCtl delViewCtl = fxmlLoader.getController();
            location = this.getClass().getResource("/com/os/apps/fileApp/res/tip.png");
            primaryStage.getIcons().add(new Image(String.valueOf(location)));
            scene.setFill(Color.TRANSPARENT);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.show();
            delViewCtl.init(primaryStage, this.mainView, this.tipString, this.block);
            primaryStage.setResizable(false);
        }
    }
}
