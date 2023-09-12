package com.os.apps.fileApp.app;

import com.os.apps.fileApp.Controller.FileViewCtl;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.FAT;
import com.os.utils.fileSystem.File;
import com.os.utils.fileSystem.Folder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FileView extends Application {
    private final File file;
    private final Disk block;
    private String newContent;
    private String oldContent;
    private final Stage stage;
    public static Map<File, Stage> maps = new HashMap<>();
    FXMLLoader fxmlLoader;
    FileViewCtl fileViewCtl;
    private final Parent root;

    public FileView(Stage stage, File file, Disk block) throws IOException {
        fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/os/apps/fileApp/fxmls/FileView.fxml"));
        this.root = this.fxmlLoader.load();
        this.file = file;
        this.block = block;
        this.stage = stage;
        this.saveContent(file.getContent());
    }

    private void showView() {
        fileViewCtl = this.fxmlLoader.getController();
        fileViewCtl.title.setText(this.file.getLocation() + "\\" + this.file.getFileName());

        fileViewCtl.contentField.setText(this.file.getContent());
        if (this.file.getFlag() == 0) {
            fileViewCtl.contentField.setDisable(true);
        }

        URL location = getClass().getResource("/com/os/apps/fileApp/res/save.png");
        fileViewCtl.saveItem.setGraphic(new ImageView(String.valueOf(location)));
        ((ImageView) fileViewCtl.saveItem.getGraphic()).setFitWidth(15.0);
        ((ImageView) fileViewCtl.saveItem.getGraphic()).setFitHeight(15.0);
        fileViewCtl.saveItem.setOnAction((ActionEvent) -> {
            this.newContent = fileViewCtl.contentField.getText();
            this.oldContent = this.file.getContent();
            if (this.newContent == null) {
                this.newContent = "";
            }

            if (!this.newContent.equals(this.oldContent)) {
                this.saveContent(this.newContent);
            }

        });

        location = getClass().getResource("/com/os/apps/fileApp/res/save.png");
        fileViewCtl.save_close.setGraphic(new ImageView(String.valueOf(location)));
        ((ImageView) fileViewCtl.save_close.getGraphic()).setFitWidth(15.0);
        ((ImageView) fileViewCtl.save_close.getGraphic()).setFitHeight(15.0);
        fileViewCtl.save_close.setOnAction((ActionEvent) -> {
            this.newContent = fileViewCtl.contentField.getText();
            this.saveContent(this.newContent);
            FAT.removeOpenedFile(this.block);
            this.stage.close();
        });

        location = getClass().getResource("/com/os/apps/fileApp/res/close.png");
        fileViewCtl.closeItem.setGraphic(new ImageView(String.valueOf(location)));
        ((ImageView) fileViewCtl.closeItem.getGraphic()).setFitWidth(15.0);
        ((ImageView) fileViewCtl.closeItem.getGraphic()).setFitHeight(15.0);
        fileViewCtl.closeItem.setOnAction((ActionEvent) -> {
            FAT.removeOpenedFile(this.block);
            this.stage.close();
        });
        Scene scene = new Scene(this.root);
        this.stage.setScene(scene);
        this.stage.setTitle(this.file.getFileName());
        this.stage.titleProperty().bind(this.file.fileNamePProperty());
        location = getClass().getResource("/com/os/apps/fileApp/res/file.png");
        this.stage.getIcons().add(new Image(String.valueOf(location)));

        MainUI.fileAppAdditionStageList.add(this.stage);

        scene.setFill(Color.TRANSPARENT);
        this.stage.initStyle(StageStyle.TRANSPARENT);

        fileViewCtl.init(this.file, this.stage, this.block);
        this.stage.show();
        maps.put(this.file, this.stage);
        System.out.println(maps.get(this.file));
    }

    private void saveContent(String newContent) {
        int newLength = newContent.length();
        int blockCount = FAT.blocksCount(newLength);
        this.file.setLength(blockCount);
        this.file.setContent(newContent);
        this.file.setSize(FAT.getSize(newLength));
        if (this.file.hasParent()) {
            Folder parent = this.file.getParent();
            parent.setSize(FAT.getFolderSize(parent));

            while (parent.hasParent()) {
                parent = parent.getParent();
                parent.setSize(FAT.getFolderSize(parent));
            }
        }

        MainUI.fat.reallocBlocks(blockCount, this.block);
    }

    public void start(Stage primaryStage) throws Exception {
        this.showView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
