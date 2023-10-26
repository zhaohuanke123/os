package com.os.applications.fileApp.application;

import com.os.applications.BaseApp;
import com.os.applications.fileApp.controller.FileApplicationController;
import com.os.utility.fileSystem.*;
import com.os.utility.fileSystem.File;
import com.os.utility.uiUtil.CompSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FileApplication extends BaseApp<FileApplicationController> {
    public static FAT fat;
    private TreeItem<String> recentNode;
    private int ind;
    public static Disk copyBlock;
    public static boolean copyFlag;
    public static boolean moveFlag = false;
    public static Vector<Stage> fileAppAdditionStageList = new Vector<>();
    private List<Disk> blockList;
    public String recentPath;  // 记录当前路径
    public static File copyFile;
    private final Map<Path, TreeItem<String>> pathMap = new HashMap<>();
    private ContextMenu contextMenu1;  // 文件管理器的右键菜单
    private ContextMenu contextMenu2;  // 文件/目录的右键菜单
    private MenuItem createFileItem;  // 新建文件
    private MenuItem createFolderItem;  // 新建目录
    private MenuItem pasteItem;  // 粘贴
    private MenuItem openItem;  // 打开
    private MenuItem delItem;  // 删除
    private MenuItem propItem;  // 属性
    private MenuItem copyItem;  // 复制
    private MenuItem moveItem;  // 剪切
    private Label[] icons;
    public static boolean clearFlag = false;

    public FileApplication() {
        super("/com/os/applications/fileApp/fxmls/FileApp.fxml",
                "/com/os/applications/fileApp/res/folder.png",
                "磁盘文件管理系统",
                -1, -1);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        stage.setOnCloseRequest((e) -> saveData());

        this.recentPath = "C:";

        FAT.closeAll();
        this.treeViewInit();
        this.tableInit();
        this.menuInit();
        this.menuItemSetOnAction();
    }

    public static void minimizeOnShowApp(boolean isMinimize) {
        if (fileAppAdditionStageList != null) {
            for (int i = 0; i < fileAppAdditionStageList.size(); ++i) {
                Stage stage = fileAppAdditionStageList.get(i);
                if (stage != null) {
                    if (!stage.isShowing()) {
                        fileAppAdditionStageList.remove(stage);
                    } else {
                        stage.setIconified(isMinimize);
                    }
                }
            }
        }
    }

    // 初始化右键菜单
    private void menuInit() {
        this.createFileItem = new MenuItem("新建文件");
        this.createFolderItem = new MenuItem("新建文件夹");
        this.pasteItem = new MenuItem("粘贴");
        this.openItem = new MenuItem("打开");
        this.delItem = new MenuItem("删除");
        this.propItem = new MenuItem("属性");
        this.copyItem = new MenuItem("复制");
        this.moveItem = new MenuItem("剪切");

        this.contextMenu1 = new ContextMenu(this.createFileItem, this.createFolderItem, this.pasteItem);
        this.contextMenu2 = new ContextMenu(this.openItem, this.delItem, this.propItem, this.copyItem, this.moveItem);

        controller.flowPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (me) -> {
            // 鼠标右键点击且contextMenu2未显示，则显示contextMenu1
            if (me.getButton() == MouseButton.SECONDARY && !this.contextMenu2.isShowing()) {
                // 禁用粘贴菜单项
                this.pasteItem.setDisable(true);
                // 如果有复制标志、剪切文件，则允许使用粘贴菜单项
                if (copyFlag || moveFlag) this.pasteItem.setDisable(false);

                // 在鼠标的当前位置显示菜单
                this.contextMenu1.show(controller.flowPane, me.getScreenX(), me.getScreenY());
            } else {
                // 隐藏contextMenu1
                this.contextMenu1.hide();
            }
        });
    }

    private void tableInit() {
        var disk = controller.diskTable.getColumns().get(0);
        var index = controller.diskTable.getColumns().get(1);
        var type = controller.diskTable.getColumns().get(2);
        var content = controller.diskTable.getColumns().get(3);

        var fileName = controller.openFile.getColumns().get(0);
        var openType = controller.openFile.getColumns().get(1);
        var beginDisk = controller.openFile.getColumns().get(2);
        var fileLength = controller.openFile.getColumns().get(3);
        var filePath = controller.openFile.getColumns().get(4);

        disk.setCellValueFactory(new PropertyValueFactory<>("numP"));
        index.setCellValueFactory(new PropertyValueFactory<>("indexP"));
        type.setCellValueFactory(new PropertyValueFactory<>("typeP"));
        content.setCellValueFactory(new PropertyValueFactory<>("objectP"));

        ObservableList<Disk> disksItem = FXCollections.observableArrayList(fat.getDiskBlocks());
        controller.diskTable.setItems(disksItem);

        fileName.setCellValueFactory(new PropertyValueFactory<>("NameP"));
        openType.setCellValueFactory(new PropertyValueFactory<>("flagP"));
        beginDisk.setCellValueFactory(new PropertyValueFactory<>("diskNumP"));
        fileLength.setCellValueFactory(new PropertyValueFactory<>("lengthP"));
        filePath.setCellValueFactory(new PropertyValueFactory<>("locationP"));

        ObservableList<File> fileOpened = fat.getOpenedFiles();
        controller.openFile.setItems(fileOpened);
    }

    private void treeViewInit() {
        URL location = this.getClass().getResource("/com/os/applications/fileApp/res/disk.png");
        TreeItem<String> rootNode = new TreeItem<>("C:", new ImageView(String.valueOf(location)));
        CompSet.setImageViewFixSize((ImageView) rootNode.getGraphic(), 20.0, 20.0);

        rootNode.setExpanded(true);
        this.recentNode = rootNode;
        this.pathMap.put(fat.getPath("C:"), rootNode);
        controller.treeView.setRoot(rootNode);
        controller.treeView.setCellFactory((p) -> new TextFieldTreeCellImpl());

        for (Path path : fat.getPaths()) {
            if (path.hasParent() && path.getParent().getPathName().equals(rootNode.getValue())) {
                this.TreeNodeInit(path, rootNode);
            }
        }

        this.addIcon(fat.getBlockList(this.recentPath), this.recentPath);
    }

    private void TreeNodeInit(Path newPath, TreeItem<String> parentNode) {
        TreeItem<String> newNode = this.addNode(parentNode, newPath);
        if (newPath.hasChild()) {
            for (Path child : newPath.getChildren()) {
                this.TreeNodeInit(child, newNode);
            }
        }
    }

    private TreeItem<String> addNode(TreeItem<String> parentNode, Path newPath) {
        String pathName = newPath.getPathName();
        String value = pathName.substring(pathName.lastIndexOf(92) + 1);
        URL location = this.getClass().getResource("/com/os/applications/fileApp/res/node.png");
        TreeItem<String> newNode = new TreeItem<>(value, new ImageView(String.valueOf(location)));
        newNode.setExpanded(true);
        this.pathMap.put(newPath, newNode);
        ((ImageView) newNode.getGraphic()).setFitWidth(15.0);
        ((ImageView) newNode.getGraphic()).setFitHeight(15.0);
        parentNode.getChildren().add(newNode);
        return newNode;
    }

    public void removeNode(TreeItem<String> recentNode, Path remPath) {
        recentNode.getChildren().remove(this.pathMap.get(remPath));
        this.pathMap.remove(remPath);
    }

    public void addIcon(List<Disk> bList, String path) {
        this.blockList = bList;
        final int n = bList.size();
        this.icons = new Label[n];

        for (int i = 0; i < n; ++i) {
            String nName;
            if (bList.get(i).getObject() instanceof Folder) {
                nName = ((Folder) bList.get(i).getObject()).getName();
                if (nName.length() > 4) {
                    nName = nName.substring(0, 4) + "..";
                }

                URL location = this.getClass().getResource("/com/os/applications/fileApp/res/folder.png");
                this.icons[i] = new Label(nName, new ImageView(String.valueOf(location)));
            } else {
                nName = ((File) bList.get(i).getObject()).getName();
                if (nName.length() > 4) {
                    nName = nName.substring(0, 4) + "..";
                }

                URL location = this.getClass().getResource("/com/os/applications/fileApp/res/file.png");
                this.icons[i] = new Label(nName, new ImageView(String.valueOf(location)));
            }
            this.icons[i].setPrefSize(100.0, 100.0);
            this.icons[i].setMinSize(100.0, 100.0);
            this.icons[i].setMaxSize(100.0, 100.0);
            this.icons[i].setAlignment(Pos.CENTER);
            ((ImageView) this.icons[i].getGraphic()).setFitWidth(60.0);
            ((ImageView) this.icons[i].getGraphic()).setFitHeight(60.0);

            this.icons[i].setContentDisplay(ContentDisplay.TOP);
            this.icons[i].setWrapText(true);
            controller.flowPane.getChildren().add(this.icons[i]);
            this.icons[i].setOnMouseEntered(event -> {
                Label src = (Label) event.getSource();

                for (int j = 0; j < n; ++j) {
                    if (src == FileApplication.this.icons[j]) {
                        FileApplication.this.ind = j;
                    }
                }

                Disk thisBlock = FileApplication.this.blockList.get(FileApplication.this.ind);
                Tooltip.install(controller.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: rgb(103, 157, 238, 0.5); " +
                        "-fx-background-radius: 12;");
            });
            this.icons[i].setOnMouseExited(event -> {
                Disk thisBlock = FileApplication.this.blockList.get(FileApplication.this.ind);
                Tooltip.uninstall(controller.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: transparent;");
            });
            this.icons[i].setOnMouseClicked(event -> {
                Label src = (Label) event.getSource();

                for (int j = 0; j < n; ++j) {
                    if (src == FileApplication.this.icons[j]) {
                        FileApplication.this.ind = j;
                    }
                }

                FileApplication.this.copyItem.setDisable(false);
                FileApplication.this.moveItem.setDisable(false);
                FileApplication.this.createFileItem.setDisable(false);
                if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                    Disk thisBlock = FileApplication.this.blockList.get(FileApplication.this.ind);
                    if (thisBlock.getType().equals("文件夹")) {
                        FileApplication.this.copyItem.setDisable(true);
                        FileApplication.this.moveItem.setDisable(true);
                    }

                    FileApplication.this.contextMenu2.show(src, event.getScreenX(), event.getScreenY());
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    try {
                        FileApplication.this.onOpen();
                    } catch (IOException e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    FileApplication.this.contextMenu2.hide();
                }

            });
        }

    }

    private void onOpen() throws IOException {
        Disk thisBlock = this.blockList.get(this.ind);

        if (thisBlock.getObject() instanceof File) {
            if (fat.getOpenedFiles().size() < 5) {
                if (fat.isOpenedFile(thisBlock)) {
                    try {
                        fileViewOpen((File) thisBlock.getObject(), thisBlock);
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    try {
                        fileViewOpen((File) thisBlock.getObject(), thisBlock);
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }

                    fat.addOpenedFile(thisBlock);
                }
            } else {
                try {
                    tipOpen("文件打开已到上限");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        } else {
            Folder thisFolder = (Folder) thisBlock.getObject();
            String newPath = thisFolder.getLocation() + "\\" + thisFolder.getName();
            controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
            this.addIcon(fat.getBlockList(newPath), newPath);
            controller.currentPath.setText(newPath);
            this.recentPath = newPath;
            this.recentNode = this.pathMap.get(thisFolder.getPath());
        }

    }

    public TreeItem<String> getRecentNode() {
        return this.recentNode;
    }

    private void menuItemSetOnAction() {
        this.createFileItem.setOnAction((ActionEvent) -> {
            if (((Folder) fat.getDiskBlocks()[2].getObject()).getCatalogNum() > 7 && controller.currentPath.getText().equals("C:")) {
                try {
                    tipOpen("根路径最多创建\n8个目录项");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }

            } else {
                int no = fat.createFile(this.recentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法创建");
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.recentPath), this.recentPath);
                }

            }
        });
        this.createFolderItem.setOnAction((ActionEvent) -> {
            if (((Folder) fat.getDiskBlocks()[2].getObject()).getCatalogNum() > 7 && controller.currentPath.getText().equals("C:")) {
                try {
                    tipOpen("根路径最多创建\n8个目录项");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }

            } else {
                int no = fat.createFolder(this.recentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法创建");
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    Folder newFolder = (Folder) fat.getBlock(no).getObject();
                    Path newPath = newFolder.getPath();
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.recentPath), this.recentPath);
                    this.addNode(this.recentNode, newPath);
                }

            }
        });
        this.openItem.setOnAction((ActionEvent) -> {
            try {
                this.onOpen();
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

        });
        this.delItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.ind);
            if (fat.isOpenedFile(thisBlock)) {
                try {
                    tipOpen("文件未关闭");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            } else {
                try {
                    this.delViewOpen(thisBlock);
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }

        });
        this.propItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.ind);

            try {
                this.propertyOpen(thisBlock, this.icons[this.ind], this.pathMap);
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }

        });
        this.copyItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.ind);
            copyBlock = thisBlock;
            copyFile = (File) thisBlock.getObject();
            copyFlag = true;
        });
        this.moveItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.ind);
            copyBlock = thisBlock;
            copyFile = (File) thisBlock.getObject();
            copyFlag = true;
            moveFlag = true;
        });
        this.pasteItem.setOnAction((ActionEvent) -> {
            if (copyFlag) {
                int no = fat.createFile(this.recentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法粘贴");
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    if (moveFlag) {
                        fat.delete(copyBlock);
                        moveFlag = false;
                    }

                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.recentPath), this.recentPath);
                }

                copyFlag = false;
            }

        });
    }

    public void propertyOpen(Disk thisBlock, Label icon, Map<Path, TreeItem<String>> pathMap) throws Exception {
        Stage stage = new Stage();
        PropertyApplication propertyApplication = null;

        try {
            propertyApplication = new PropertyApplication(thisBlock, icon, pathMap);
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        if (propertyApplication != null) {
            propertyApplication.start(stage);
        }
    }

    public static void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication(tipString);
        tipWindow.start(stage);
        Text text = new Text(tipString);
        text.setFill(javafx.scene.paint.Color.RED);
        text.setFont(javafx.scene.text.Font.font("宋体", 25.0D));
        tipWindow.controller.tipTextFlow.getChildren().add(text);
    }

    public static void fileViewOpen(File file, Disk block) {
        if (file.isOpened()) {
            FileEditApplication.maps.get(file).show();
            FileEditApplication.maps.get(file).setAlwaysOnTop(true);
            FileEditApplication.maps.get(file).setIconified(false);
            FileEditApplication.maps.get(file).toFront();
        }

        if (!file.isOpened()) {
            Stage stage = new Stage();
            FileEditApplication fileView = new FileEditApplication(stage, file, block);
            try {
                fileView.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void delViewOpen(Disk block) throws Exception {
        Stage stage = new Stage();
        DeleteDialogApplication deleteDialogApplication = new DeleteDialogApplication(this, block);
        deleteDialogApplication.start(stage);
    }

    // 加载数据
    public static void loadData() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./data"));
            Throwable e1 = null;

            try {
                fat = (FAT) inputStream.readObject();
            } catch (Throwable e2) {
                e1 = e2;
                throw e2;
            } finally {
                if (e1 != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e3) {
                        e1.addSuppressed(e3);
                    }
                } else {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        // 如果没有fat，则新建fat
        if (fat == null || clearFlag) {
            fat = new FAT();
            clearFlag = false;
        }
    }

    public static void saveData() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data"));
            Throwable e1 = null;

            try {
                outputStream.writeObject(fat);
            } catch (Throwable e2) {
                e1 = e2;
                throw e2;
            } finally {
                if (e1 != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable e3) {
                        e1.addSuppressed(e3);
                    }
                } else {
                    outputStream.close();
                }

            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    public final class TextFieldTreeCellImpl extends TreeCell<String> {
        private TextField textField;

        public TextFieldTreeCellImpl() {
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && TextFieldTreeCellImpl.this.getTreeItem() != null) {
                    String pathName = null;

                    for (Map.Entry<Path, TreeItem<String>> pathTreeItemEntry : FileApplication.this.pathMap.entrySet()) {
                        if (TextFieldTreeCellImpl.this.getTreeItem() == pathTreeItemEntry.getValue()) {
                            pathName = pathTreeItemEntry.getKey().getPathName();
                            break;
                        }
                    }

                    List<Disk> fats = FileApplication.fat.getBlockList(pathName);
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    FileApplication.this.addIcon(fats, pathName);
                    FileApplication.this.recentPath = pathName;
                    FileApplication.this.recentNode = TextFieldTreeCellImpl.this.getTreeItem();
                    controller.currentPath.setText(FileApplication.this.recentPath);
                }

            });
        }

        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setText(null);
                this.setGraphic(null);
            } else if (this.isEditing()) {
                if (this.textField != null) {
                    this.textField.setText(this.getString());
                }

                this.setText(null);
                this.setGraphic(this.textField);
            } else {
                this.setText(this.getString());
                this.setGraphic(this.getTreeItem().getGraphic());
            }

        }

        private String getString() {
            return this.getItem() == null ? "" : this.getItem();
        }
    }
}
