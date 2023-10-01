package com.os.apps.fileApp;

import com.os.apps.BaseApp;
import com.os.apps.fileApp.app.DelView;
import com.os.apps.fileApp.app.FileView;
import com.os.apps.fileApp.app.PropertyView;
import com.os.apps.fileApp.app.TipWindow;
import com.os.utils.fileSystem.*;
import com.os.utils.fileSystem.File;
import com.os.utils.uiUtil.CompSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FileApp extends BaseApp<FileAppController> {
    public static FAT fat;
    private TreeItem<String> rootNode;
    private TreeItem<String> recentNode;
    private int ind;
    public static Disk copyBlock;
    public static boolean copyFlag;
    public static boolean moveFlag = false;
    public static Vector<Stage> fileAppAdditionStageList = new Vector<>();
    private List<Disk> blockList;
    public String recentPath;
    public static File copyFile;
    private final Map<Path, TreeItem<String>> pathMap = new HashMap<>();
    private ObservableList<Disk> disksItem;
    private ObservableList<File> fileOpened;
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

    public FileApp() {
        super("/com/os/apps/fileApp/fxmls/mainUI.fxml",
                "/com/os/apps/fileApp/res/folder.png",
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

        this.disksItem = FXCollections.observableArrayList(fat.getDiskBlocks());
        controller.diskTable.setItems(this.disksItem);

        fileName.setCellValueFactory(new PropertyValueFactory<>("NameP"));
        openType.setCellValueFactory(new PropertyValueFactory<>("flagP"));
        beginDisk.setCellValueFactory(new PropertyValueFactory<>("diskNumP"));
        fileLength.setCellValueFactory(new PropertyValueFactory<>("lengthP"));
        filePath.setCellValueFactory(new PropertyValueFactory<>("locationP"));

        this.fileOpened = fat.getOpenedFiles();
        controller.openFile.setItems(this.fileOpened);
    }

    private void treeViewInit() {
        URL location = this.getClass().getResource("/com/os/apps/fileApp/res/disk.png");
        this.rootNode = new TreeItem<>("C:", new ImageView(String.valueOf(location)));
        CompSet.setImageViewFixSize((ImageView) this.rootNode.getGraphic(), 20.0, 20.0);

        this.rootNode.setExpanded(true);
        this.recentNode = this.rootNode;
        this.pathMap.put(fat.getPath("C:"), this.rootNode);
        controller.treeView.setRoot(this.rootNode);
        controller.treeView.setCellFactory((p) -> new TextFieldTreeCellImpl());

        for (Path path : fat.getPaths()) {
            System.out.println(path);

            if (path.hasParent() && path.getParent().getPathName().equals(this.rootNode.getValue())) {
                this.TreeNodeInit(path, this.rootNode);
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
        URL location = this.getClass().getResource("/com/os/apps/fileApp/res/node.png");
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

                URL location = this.getClass().getResource("/com/os/apps/fileApp/res/folder.png");
                this.icons[i] = new Label(nName, new ImageView(String.valueOf(location)));
            } else {
                nName = ((File) bList.get(i).getObject()).getName();
                if (nName.length() > 4) {
                    nName = nName.substring(0, 4) + "..";
                }

                URL location = this.getClass().getResource("/com/os/apps/fileApp/res/file.png");
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
                    if (src == FileApp.this.icons[j]) {
                        FileApp.this.ind = j;
                    }
                }

                Disk thisBlock = FileApp.this.blockList.get(FileApp.this.ind);
                Tooltip.install(controller.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: rgba(240,248,255,0.5); " +
                        "-fx-background-radius: 12;");
            });
            this.icons[i].setOnMouseExited(event -> {
                Disk thisBlock = FileApp.this.blockList.get(FileApp.this.ind);
                Tooltip.uninstall(controller.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: rgba(240,248,255,0);");
            });
            this.icons[i].setOnMouseClicked(event -> {
                Label src = (Label) event.getSource();

                for (int j = 0; j < n; ++j) {
                    if (src == FileApp.this.icons[j]) {
                        FileApp.this.ind = j;
                    }
                }

                FileApp.this.copyItem.setDisable(false);
                FileApp.this.moveItem.setDisable(false);
                FileApp.this.createFileItem.setDisable(false);
                if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                    Disk thisBlock = FileApp.this.blockList.get(FileApp.this.ind);
                    if (thisBlock.getType().equals("文件夹")) {
                        FileApp.this.copyItem.setDisable(true);
                        FileApp.this.moveItem.setDisable(true);
                    }

                    FileApp.this.contextMenu2.show(src, event.getScreenX(), event.getScreenY());
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    try {
                        FileApp.this.onOpen();
                    } catch (IOException var4) {
                        System.out.println(Arrays.toString(var4.getStackTrace()));
                        System.out.println(Arrays.toString(var4.getStackTrace()));
                    }
                } else {
                    FileApp.this.contextMenu2.hide();
                }

            });
        }

    }

    private void onOpen() throws IOException {
        Disk thisBlock = this.blockList.get(this.ind);

        for (Disk block : this.blockList) {
            System.out.println(block);
        }

        if (thisBlock.getObject() instanceof File) {
            if (fat.getOpenedFiles().size() < 5) {
                if (fat.isOpenedFile(thisBlock)) {
                    try {
                        fileViewOpen((File) thisBlock.getObject(), thisBlock);
                    } catch (Exception var6) {
                        System.out.println(Arrays.toString(var6.getStackTrace()));
                    }
                } else {
                    try {
                        fileViewOpen((File) thisBlock.getObject(), thisBlock);
                    } catch (Exception var5) {
//                        System.out.println(Arrays.toString(var5.getStackTrace()));
                        var5.printStackTrace();
                    }

                    fat.addOpenedFile(thisBlock);
                }
            } else {
                try {
                    tipOpen("文件打开已到上限");
                } catch (Exception var4) {
                    System.out.println(Arrays.toString(var4.getStackTrace()));
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
                } catch (Exception var4) {
                    System.out.println(Arrays.toString(var4.getStackTrace()));
                }

            } else {
                int no = fat.createFile(this.recentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法创建");
                    } catch (Exception var5) {
                        System.out.println(Arrays.toString(var5.getStackTrace()));
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
                } catch (Exception var5) {
                    System.out.println(Arrays.toString(var5.getStackTrace()));
                }

            } else {
                int no = fat.createFolder(this.recentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法创建");
                    } catch (Exception var6) {
                        System.out.println(Arrays.toString(var6.getStackTrace()));
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
            } catch (IOException var3) {
                System.out.println(Arrays.toString(var3.getStackTrace()));
            }

        });
        this.delItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.ind);
            if (fat.isOpenedFile(thisBlock)) {
                try {
                    tipOpen("文件未关闭");
                } catch (Exception var5) {
                    System.out.println(Arrays.toString(var5.getStackTrace()));
                }
            } else {
                try {
                    this.delViewOpen(thisBlock);
                } catch (Exception var4) {
                    System.out.println(Arrays.toString(var4.getStackTrace()));
                }
            }

        });
        this.propItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.ind);

            try {
                this.propertyOpen(thisBlock, this.icons[this.ind], this.pathMap);
            } catch (Exception var4) {
                System.out.println(Arrays.toString(var4.getStackTrace()));
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
                    } catch (Exception var4) {
                        System.out.println(Arrays.toString(var4.getStackTrace()));
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
        PropertyView propertyView = null;

        try {
            propertyView = new PropertyView(thisBlock, icon, pathMap);
        } catch (IOException var7) {
            System.out.println(Arrays.toString(var7.getStackTrace()));
        }

        if (propertyView != null) {
            propertyView.start(stage);
        }
    }

    public static void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipWindow tipWindow = new TipWindow(tipString);
        tipWindow.start(stage);
    }

    public static void fileViewOpen(File file, Disk block) {
        System.out.println("fileViewOpen" + file.isOpened());
        if (file.isOpened()) {
            FileView.maps.get(file).show();
            FileView.maps.get(file).setAlwaysOnTop(true);
            FileView.maps.get(file).setIconified(false);
            FileView.maps.get(file).toFront();
        }

        if (!file.isOpened()) {
            Stage stage = new Stage();
            FileView fileView = new FileView(stage, file, block);
            try {
                fileView.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("fileViewOpen" + file.isOpened());
        }

    }

    public void delViewOpen(Disk block) throws Exception {
        Stage stage = new Stage();
        DelView delView = new DelView(this, block);
        delView.start(stage);
    }

    // 加载数据
    public static void loadData() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./data"));
            Throwable var2 = null;

            try {
                fat = (FAT) inputStream.readObject();
            } catch (Throwable var12) {
                var2 = var12;
                throw var12;
            } finally {
                if (var2 != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
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
            Throwable var1 = null;

            try {
                System.out.println("正在保存磁盘文件数据");
                outputStream.writeObject(fat);
            } catch (Throwable var12) {
                var1 = var12;
                throw var12;
            } finally {
                if (var1 != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable var11) {
                        var1.addSuppressed(var11);
                    }
                } else {
                    outputStream.close();
                }

            }
        } catch (IOException var15) {
            System.out.println(Arrays.toString(var15.getStackTrace()));
        }

    }

    public final class TextFieldTreeCellImpl extends TreeCell<String> {
        private TextField textField;

        public TextFieldTreeCellImpl() {
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && TextFieldTreeCellImpl.this.getTreeItem() != null) {
                    String pathName = null;

                    for (Map.Entry<Path, TreeItem<String>> pathTreeItemEntry : FileApp.this.pathMap.entrySet()) {
                        if (TextFieldTreeCellImpl.this.getTreeItem() == pathTreeItemEntry.getValue()) {
                            pathName = pathTreeItemEntry.getKey().getPathName();
                            break;
                        }
                    }

                    List<Disk> fats = FileApp.fat.getBlockList(pathName);
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    FileApp.this.addIcon(fats, pathName);
                    FileApp.this.recentPath = pathName;
                    System.out.println(pathName);
                    FileApp.this.recentNode = TextFieldTreeCellImpl.this.getTreeItem();
                    controller.currentPath.setText(FileApp.this.recentPath);
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
