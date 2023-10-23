package com.os.applications.fileApp.application;

import com.os.applications.fileApp.controller.FileEditController;
import com.os.applications.fileApp.FileApplication;
import com.os.utility.fileSystem.Disk;
import com.os.utility.fileSystem.FAT;
import com.os.utility.fileSystem.File;
import com.os.utility.fileSystem.Folder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FileEditApplication extends BaseFileApplication<FileEditController> {
    private final File file;
    private final Disk block;
    private String newContent;
    private String oldContent;
    private final Stage stage;
    public static Map<File, Stage> maps = new HashMap<>();

    public FileEditApplication(Stage stage, File file, Disk block)  {
        super(
                "/com/os/applications/fileApp/fxmls/FileEdit.fxml",
                "/com/os/applications/fileApp/res/file.png",
                "文件",
                500,
                500
        );

        this.file = file;
        this.block = block;
        this.stage = stage;
        this.saveContent(file.getContent());

        super.toolTip = file.getName();
    }

    private void showView() {
        controller.title.setText(this.file.getLocation() + "\\" + this.file.getName());

        controller.contentField.setText(this.file.getContent());
        if (this.file.getFlag() == 0) {
            controller.contentField.setDisable(true);
        }

        URL location = getClass().getResource("/com/os/applications/fileApp/res/save.png");
        controller.saveItem.setGraphic(new ImageView(String.valueOf(location)));
        ((ImageView) controller.saveItem.getGraphic()).setFitWidth(15.0);
        ((ImageView) controller.saveItem.getGraphic()).setFitHeight(15.0);
        controller.saveItem.setOnAction((ActionEvent) -> {
            this.newContent = controller.contentField.getText();
            this.oldContent = this.file.getContent();
            if (this.newContent == null) {
                this.newContent = "";
            }

            if (!this.newContent.equals(this.oldContent)) {
                this.saveContent(this.newContent);
            }

        });

        location = getClass().getResource("/com/os/applications/fileApp/res/save.png");
        controller.save_close.setGraphic(new ImageView(String.valueOf(location)));
        ((ImageView) controller.save_close.getGraphic()).setFitWidth(15.0);
        ((ImageView) controller.save_close.getGraphic()).setFitHeight(15.0);
        controller.save_close.setOnAction((ActionEvent) -> {
            this.newContent = controller.contentField.getText();
            this.saveContent(this.newContent);
            FAT.removeOpenedFile(this.block);
            this.stage.close();
        });

        location = getClass().getResource("/com/os/applications/fileApp/res/close.png");
        controller.closeItem.setGraphic(new ImageView(String.valueOf(location)));
        ((ImageView) controller.closeItem.getGraphic()).setFitWidth(15.0);
        ((ImageView) controller.closeItem.getGraphic()).setFitHeight(15.0);
        controller.closeItem.setOnAction((ActionEvent) -> {
            FAT.removeOpenedFile(this.block);
            this.stage.close();
        });

        controller.init(this.file, this.stage, this.block);
        maps.put(this.file, this.stage);
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

        FileApplication.fat.reallocBlocks(blockCount, this.block);
    }

    public void start(Stage stage) throws IOException {
        super.start(stage);

        this.showView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
