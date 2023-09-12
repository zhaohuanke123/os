package com.os.apps.fileApp.app;

import com.os.apps.fileApp.Controller.PropertyCtl;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.File;
import com.os.utils.fileSystem.Folder;
import com.os.utils.fileSystem.Path;
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
import java.util.Map;
import java.util.regex.Pattern;

public class PropertyView extends Application {
    private final Disk block;
    private final Label icon;
    private final Map<Path, TreeItem<String>> pathMap;
    private String oldName;
    private String location;
    private Stage stage;
    private TextField nameField;
    private Label typeField;
    private Label locField;
    private Label spaceField;
    private Label timeField;
    private Button okBtn;
    private Button cancelBtn;
    private Button applyBtn;
    private RadioButton checkRead;
    private RadioButton checkWrite;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private Image ico;
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/os/apps/fileApp/fxmls/PropertyView.fxml"));
    private final Parent root;

    public PropertyView(Disk block, Label icon, Map<Path, TreeItem<String>> pathMap, Stage stage) throws IOException {
        this.root = this.fxmlLoader.load();
        this.block = block;
        this.icon = icon;
        this.pathMap = pathMap;
        this.stage = stage;
    }

    private void showView() {
        this.nameField = (TextField) this.root.lookup("#na");
        this.typeField = (Label) this.root.lookup("#type");
        this.locField = (Label) this.root.lookup("#loc");
        this.spaceField = (Label) this.root.lookup("#space");
        this.timeField = (Label) this.root.lookup("#time");
        this.okBtn = (Button) this.root.lookup("#yes");
        this.cancelBtn = (Button) this.root.lookup("#no");
        this.applyBtn = (Button) this.root.lookup("#apply");
        this.checkRead = (RadioButton) this.root.lookup("#read");
        this.checkRead.setToggleGroup(this.toggleGroup);
        this.checkRead.setUserData(0);
        this.checkWrite = (RadioButton) this.root.lookup("#write");
        this.checkWrite.setToggleGroup(this.toggleGroup);
        this.checkWrite.setUserData(1);
        this.toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> this.applyBtn.setDisable(false));
        Label icon = (Label) this.root.lookup("#propertyIcon");
        ImageView imageView;
        if (this.block.getObject() instanceof Folder) {
            Folder folder = (Folder) this.block.getObject();
            this.nameField.setText(folder.getFolderName());
            this.typeField.setText(folder.getType());
            this.locField.setText(folder.getLocation());
            this.spaceField.setText(folder.getSpace());
            this.timeField.setText(folder.getCreateTime());
            this.oldName = folder.getFolderName();
            this.location = folder.getLocation();
            this.checkRead.setDisable(true);
            this.checkWrite.setDisable(true);
            this.ico = new Image("com/os/apps/fileApp/res/folder.png");
        } else {
            File file = (File) this.block.getObject();
            this.nameField.setText(file.getFileName());
            this.typeField.setText(file.getType());
            this.locField.setText(file.getLocation());
            this.spaceField.setText(file.getSpace());
            this.timeField.setText(file.getCreateTime());
            this.oldName = file.getFileName();
            this.location = file.getLocation();
            this.toggleGroup.selectToggle(file.getFlag() == 0 ? this.checkRead : this.checkWrite);
            this.ico = new Image("com/os/apps/fileApp/res/file.png");
        }
        imageView = new ImageView(this.ico);
        imageView.setFitWidth(25.0);
        imageView.setFitHeight(25.0);
        icon.setGraphic(imageView);

        this.okBtn.setOnMouseEntered(event -> PropertyView.this.okBtn.setStyle("-fx-background-color: #808080;"));
        this.okBtn.setOnMouseExited(event -> PropertyView.this.okBtn.setStyle("-fx-background-color: #d3d3d3;"));
        this.cancelBtn.setOnMouseEntered(event -> PropertyView.this.cancelBtn.setStyle("-fx-background-color: #808080;"));
        this.cancelBtn.setOnMouseExited(event -> PropertyView.this.cancelBtn.setStyle("-fx-background-color: #d3d3d3;"));
        this.applyBtn.setOnMouseEntered(event -> PropertyView.this.applyBtn.setStyle("-fx-background-color: #808080;"));
        this.applyBtn.setOnMouseExited(event -> PropertyView.this.applyBtn.setStyle("-fx-background-color: #d3d3d3;"));
        this.nameField.addEventFilter(MouseDragEvent.MOUSE_PRESSED, (event) -> this.applyBtn.setDisable(false));
        this.buttonOnAction();
        Scene scene = new Scene(this.root);
        this.stage = new Stage();
        MainUI.fileAppAdditionStageList.add(this.stage);
        scene.setFill(Color.TRANSPARENT);
        this.stage.initStyle(StageStyle.TRANSPARENT);
        PropertyCtl propertyCtl = this.fxmlLoader.getController();
        propertyCtl.init(this.stage);
        this.stage.setScene(scene);
        this.stage.setTitle("属性");
        this.stage.setResizable(false);
        this.stage.getIcons().add(this.ico);
        this.stage.setAlwaysOnTop(true);
        this.stage.show();
    }

    private void buttonOnAction() {
        this.applyBtn.setOnAction((ActionEvent) -> {
            String newName = this.nameField.getText();
            String regEx = "[$./]";
            Pattern p = Pattern.compile(regEx);
            boolean m = p.matcher(newName).find();
            if (!this.oldName.equals(newName)) {
                if (m) {
                    try {
                        System.out.println(m);
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
                    if (this.block.getObject() instanceof Folder) {
                        Folder thisFolder = (Folder) this.block.getObject();
                        thisFolder.setFolderName(newName);
                        this.pathMap.get(thisFolder.getPath()).setValue(newName);
                        this.reLoc(this.location, this.location, this.oldName, newName, thisFolder);
                    } else {
                        ((File) this.block.getObject()).setFileName(newName);
                    }

                    this.oldName = newName;
                    this.icon.setText(newName);
                }
            }

            if (this.block.getObject() instanceof File) {
                File thisFile = (File) this.block.getObject();
                int newFlag = this.toggleGroup.getSelectedToggle().getUserData().hashCode();
                System.out.println(this.toggleGroup.getSelectedToggle().getUserData());
                thisFile.setFlag(newFlag);
            }

            this.applyBtn.setDisable(true);
        });
        this.cancelBtn.setOnAction((ActionEvent) -> this.stage.close());
        this.okBtn.setOnAction((ActionEvent) -> {
            String newName = this.nameField.getText();
            String regEx = "[$./]";
            Pattern p = Pattern.compile(regEx);
            boolean m = p.matcher(newName).find();
            if (!this.oldName.equals(newName)) {
                if (m) {
                    try {
                        System.out.println(m);
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
                    if (this.block.getObject() instanceof Folder) {
                        Folder thisFolder = (Folder) this.block.getObject();
                        thisFolder.setFolderName(newName);
                        this.pathMap.get(thisFolder.getPath()).setValue(newName);
                        this.reLoc(this.location, this.location, this.oldName, newName, thisFolder);
                    } else {
                        ((File) this.block.getObject()).setFileName(newName);
                    }

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

    public void start(Stage primaryStage) throws Exception {
        this.showView();
    }
}
