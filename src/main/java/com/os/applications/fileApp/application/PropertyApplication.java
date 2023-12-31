package com.os.applications.fileApp.application;

import com.os.applications.fileApp.controller.PropertyController;
import com.os.utility.fileSystem.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class PropertyApplication extends BaseFileApplication<PropertyController> {
    private final Disk block;
    private final Label icon;
    private final Map<Path, TreeItem<String>> pathMap;
    private String oldName;
    private String location;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    public PropertyApplication(Disk block, Label icon, Map<Path, TreeItem<String>> pathMap) throws IOException {
        super(
                "/com/os/applications/fileApp/fxmls/Property.fxml",
                (block.getObject() instanceof Folder) ? "/com/os/applications/fileApp/res/folder.png" : "/com/os/applications/fileApp/res/file.png",
                "属性",
                360,
                350
        );

        this.block = block;
        this.icon = icon;
        this.pathMap = pathMap;

        this.toolTip = "属性";
    }

    public void start(Stage primaryStage) throws IOException {
        super.start(primaryStage);

        this.showView();

        super.toolTip = ((BaseFile) this.block.getObject()).getName();
    }

    private void showView() {
        controller.checkRead.setToggleGroup(this.toggleGroup);
        controller.checkRead.setUserData(0);
        controller.checkWrite.setToggleGroup(this.toggleGroup);
        controller.checkWrite.setUserData(1);
        this.toggleGroup.selectedToggleProperty().addListener(
                (ov, old_toggle, new_toggle) -> controller.applyButton.setDisable(false));

        if (this.block.getObject() instanceof Folder) {
            Folder folder = (Folder) this.block.getObject();
            controller.nameField.setText(folder.getName());
            controller.textField.setText(folder.getName());
            controller.typeField.setText(folder.getType());
            controller.locField.setText(folder.getLocation());
            controller.spaceField.setText(folder.getSpace());
            controller.timeField.setText(folder.getCreateTime());
            controller.propertyField.setVisible(false);
            controller.propertyLabel.setVisible(false);
            this.oldName = folder.getName();
            this.location = folder.getLocation();
            controller.checkRead.setVisible(false);
            controller.checkWrite.setVisible(false);
            controller.propertyLabel1.setVisible(false);
        } else {
            File file = (File) this.block.getObject();
            controller.nameField.setText(file.getName());
            controller.textField.setText(file.getName());
            controller.typeField.setText(file.getType());
            controller.locField.setText(file.getLocation());
            controller.spaceField.setText(file.getSpace());
            controller.timeField.setText(file.getCreateTime());
            controller.propertyField.setText(file.getFlag() == 0 ? "只读" : "读写");
            this.oldName = file.getName();
            this.location = file.getLocation();
            this.toggleGroup.selectToggle(file.getFlag() == 0 ? controller.checkRead : controller.checkWrite);
        }

        controller.textField.addEventFilter(MouseDragEvent.MOUSE_PRESSED, (event) -> controller.applyButton.setDisable(false));

        this.buttonOnAction();
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
                        FileApplication.tipOpen("合法目录名仅可以使用字母、数字和除“$”、“.”、“/”以外的字符");
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (FileApplication.fat.hasName(this.location, newName)) {
                    try {
                        FileApplication.tipOpen("此位置已包含同名文件/文件夹");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
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
                controller.propertyField.setText(newFlag == 0 ? "只读" : "读写");
            }

            controller.applyButton.setDisable(true);
        });
        controller.cancelButton.setOnAction((ActionEvent) -> this.controller.closeStage());
        controller.acceptButton.setOnAction((ActionEvent) -> {
            String newName = controller.textField.getText();
            String regEx = "[$./]";
            Pattern p = Pattern.compile(regEx);
            boolean m = p.matcher(newName).find();
            if (!this.oldName.equals(newName)) {
                if (m) {
                    try {
                        FileApplication.tipOpen("合法目录名仅可以使用字母、数字和除“$”、“.”、“/”以外的字符");
                        return;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (FileApplication.fat.hasName(this.location, newName)) {
                    try {
                        FileApplication.tipOpen("此位置已包含同名文件/文件夹");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
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

            controller.closeStage();
        });
    }

    private void setNewName(String newName) {
        if (this.block.getObject() instanceof Folder) {
            Folder thisFolder = (Folder) this.block.getObject();
            thisFolder.setName(newName);
            this.pathMap.get(thisFolder.getPath()).setValue(newName);
            this.reLoc(this.location, this.location, this.oldName, newName, thisFolder);
        } else {
            ((File) this.block.getObject()).setName(newName);
        }
        controller.nameField.setText(newName);
    }

    private void reLoc(String oldP, String newP, String oldN, String newN, Folder folder) {
        String oldLoc = oldP + "\\" + oldN;
        String newLoc = newP + "\\" + newN;
        Path oldPath = FileApplication.fat.getPath(oldLoc);
        FileApplication.fat.replacePath(oldPath, newLoc);

        for (Object child : folder.getChildren()) {
            if (child instanceof File) {
                ((File) child).setLocation(newLoc);
            } else {
                Folder nextFolder = (Folder) child;
                nextFolder.setLocation(newLoc);
                if (nextFolder.hasChild()) {
                    this.reLoc(oldLoc, newLoc, nextFolder.getName(), nextFolder.getName(), nextFolder);
                } else {
                    Path nextPath = FileApplication.fat.getPath(oldLoc + "\\" + nextFolder.getName());
                    String newNext = newLoc + "\\" + nextFolder.getName();
                    FileApplication.fat.replacePath(nextPath, newNext);
                }
            }
        }

    }
}
