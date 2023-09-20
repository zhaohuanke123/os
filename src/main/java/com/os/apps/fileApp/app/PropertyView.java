package com.os.apps.fileApp.app;

import com.os.apps.BaseApp;
import com.os.apps.fileApp.Controller.PropertyCtl;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.File;
import com.os.utils.fileSystem.Folder;
import com.os.utils.fileSystem.Path;
import com.os.utils.ui.CompSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

public class PropertyView extends BaseApp<PropertyCtl> {
    private final Disk block;
    private final Label icon;
    private final Map<Path, TreeItem<String>> pathMap;
    private String oldName;
    private String location;
    private Stage stage;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    public PropertyView(Disk block, Label icon, Map<Path, TreeItem<String>> pathMap, Stage stage) throws IOException {
        super(
                "/com/os/apps/fileApp/fxmls/PropertyView.fxml",
                (block.getObject() instanceof Folder) ? "/com/os/apps/fileApp/res/folder.png" : "/com/os/apps/fileApp/res/file.png",
                "属性",
                -1,
                -1
        );

        this.block = block;
        this.icon = icon;
        this.pathMap = pathMap;
        this.stage = stage;
    }

    public void start(Stage primaryStage) throws IOException {
        super.start(primaryStage);

        this.showView();
    }

    private void showView() {

        controller.checkRead.setToggleGroup(this.toggleGroup);
        controller.checkRead.setUserData(0);
        controller.checkWrite.setToggleGroup(this.toggleGroup);
        controller.checkWrite.setUserData(1);
        this.toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> controller.applyButton.setDisable(false));

        if (this.block.getObject() instanceof Folder) {
            Folder folder = (Folder) this.block.getObject();
            controller.textField.setText(folder.getFolderName());
            controller.typeField.setText(folder.getType());
            controller.locField.setText(folder.getLocation());
            controller.spaceField.setText(folder.getSpace());
            controller.timeField.setText(folder.getCreateTime());
            this.oldName = folder.getFolderName();
            this.location = folder.getLocation();
            controller.checkRead.setDisable(true);
            controller.checkWrite.setDisable(true);
        } else {
            File file = (File) this.block.getObject();
            controller.textField.setText(file.getFileName());
            controller.typeField.setText(file.getType());
            controller.locField.setText(file.getLocation());
            controller.spaceField.setText(file.getSpace());
            controller.timeField.setText(file.getCreateTime());
            this.oldName = file.getFileName();
            this.location = file.getLocation();
            this.toggleGroup.selectToggle(file.getFlag() == 0 ? controller.checkRead : controller.checkWrite);
        }

        controller.textField.addEventFilter(MouseDragEvent.MOUSE_PRESSED, (event) -> controller.applyButton.setDisable(false));

        this.buttonOnAction();
        MainUI.fileAppAdditionStageList.add(this.stage);
    }

    private void buttonOnAction() {
        controller.applyButton.setOnAction((ActionEvent) -> {
            String newName = controller.textField.getText();
            String regEx = "[$./]";
            Pattern p = Pattern.compile(regEx);
            boolean m = p.matcher(newName).find();
            if (!this.oldName.equals(newName)) {
                if (m) {
                    try {
                        MainUI.tipOpen("合法目录名仅可以使用字母、数字和除“$”、“.”、“/”以外的字符");
                        return;
                    } catch (Exception var9) {
                        System.out.println(var9.getMessage());
                    }
                }

                if (MainUI.fat.hasName(this.location, newName)) {
                    try {
                        MainUI.tipOpen("此位置已包含同名文件/文件夹");
                    } catch (Exception var8) {
                        System.out.println(var8.getMessage());
                    }
                } else {
                    setNewName(newName);

                    this.oldName = newName;
                    this.icon.setText(newName);
                }
            }

            if (this.block.getObject() instanceof File) {
                File thisFile = (File) this.block.getObject();
                int newFlag = this.toggleGroup.getSelectedToggle().getUserData().hashCode();
                thisFile.setFlag(newFlag);
            }

            controller.applyButton.setDisable(true);
        });
        controller.cancelButton.setOnAction((ActionEvent) -> this.stage.close());
        controller.acceptButton.setOnAction((ActionEvent) -> {
            String newName = controller.textField.getText();
            String regEx = "[$./]";
            Pattern p = Pattern.compile(regEx);
            boolean m = p.matcher(newName).find();
            if (!this.oldName.equals(newName)) {
                if (m) {
                    try {
                        MainUI.tipOpen("合法目录名仅可以使用字母、数字和除“$”、“.”、“/”以外的字符");
                        return;
                    } catch (Exception var9) {
                        System.out.println(var9.getMessage());
                    }
                }

                if (MainUI.fat.hasName(this.location, newName)) {
                    try {
                        MainUI.tipOpen("此位置已包含同名文件/文件夹");
                    } catch (Exception var8) {
                        System.out.println(var8.getMessage());
                    }
                } else {
                    setNewName(newName);

                    this.icon.setText(newName);
                }
            }

            if (this.block.getObject() instanceof File) {
                File thisFile = (File) this.block.getObject();
                int newFlag = this.toggleGroup.getSelectedToggle().getUserData().hashCode();
                thisFile.setFlag(newFlag);
            }

            this.stage.close();
        });
    }

    private void setNewName(String newName) {
        if (this.block.getObject() instanceof Folder) {
            Folder thisFolder = (Folder) this.block.getObject();
            thisFolder.setFolderName(newName);
            this.pathMap.get(thisFolder.getPath()).setValue(newName);
            this.reLoc(this.location, this.location, this.oldName, newName, thisFolder);
        } else {
            ((File) this.block.getObject()).setFileName(newName);
        }
    }

    private void reLoc(String oldP, String newP, String oldN, String newN, Folder folder) {
        String oldLoc = oldP + "\\" + oldN;
        String newLoc = newP + "\\" + newN;
        Path oldPath = MainUI.fat.getPath(oldLoc);
        MainUI.fat.replacePath(oldPath, newLoc);

        for (Object child : folder.getChildren()) {
            if (child instanceof File) {
                ((File) child).setLocation(newLoc);
            } else {
                Folder nextFolder = (Folder) child;
                nextFolder.setLocation(newLoc);
                if (nextFolder.hasChild()) {
                    this.reLoc(oldLoc, newLoc, nextFolder.getFolderName(), nextFolder.getFolderName(), nextFolder);
                } else {
                    Path nextPath = MainUI.fat.getPath(oldLoc + "\\" + nextFolder.getFolderName());
                    String newNext = newLoc + "\\" + nextFolder.getFolderName();
                    MainUI.fat.replacePath(nextPath, newNext);
                }
            }
        }

    }
}
