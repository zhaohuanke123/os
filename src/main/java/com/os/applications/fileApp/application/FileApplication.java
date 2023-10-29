package com.os.applications.fileApp.application;

import com.os.applications.BaseApp;
import com.os.applications.BaseController;
import com.os.applications.fileApp.controller.BaseFileController;
import com.os.applications.fileApp.controller.FileApplicationController;
import com.os.utility.fileSystem.*;
import com.os.utility.fileSystem.File;
import com.os.utility.uiUtil.CompSet;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FileApplication extends BaseApp<FileApplicationController> {
    public static FAT fat;  // 文件分配表
    private TreeItem<String> currentNode;  // 记录当前节点
    private int currentId;  // 记录当前触发事件的索引
    public static Disk copyBlock;  // 记录要复制的磁盘块
    public static boolean copyFlag;  // 记录是否可以复制
    public static boolean moveFlag = false;  // 记录是否可以剪切
    public static Vector<Stage> fileAppAdditionStageList = new Vector<>();
    public static Vector<BaseController> fileAppAdditionControllerList = new Vector<>();
    private List<Disk> blockList;  // 记录磁盘块
    public String currentPath;  // 记录当前路径
    public static File copyFile;  // 记录要复制的文件
    private final Map<Path, TreeItem<String>> pathMap = new HashMap<>();  // 记录路径和树节点的映射关系
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

    public FileApplication() {
        super("/com/os/applications/fileApp/fxmls/FileApp.fxml",
                "/com/os/applications/fileApp/res/folder.png",
                "磁盘文件管理系统",
                800,
                650);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);
        controller.formatFat.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            try {
                if (!fat.getOpenedFiles().isEmpty()) {
                    tipOpen("请关闭所有文件!");
                    return;
                }

                DeleteDialogApplication deleteDialogApplication = new DeleteDialogApplication();
                deleteDialogApplication.start(new Stage(), FileApplication.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 设置窗口关闭事件处理程序
        stage.setOnCloseRequest((e) -> saveData());
        this.currentPath = "C:";
        // 关闭所有FAT
        FAT.closeAll();
        // 初始化树视图
        this.treeViewInit();
        // 初始化表格
        this.tableInit();
        // 初始化右键菜单
        this.menuInit();
        // 设置右键菜单项的操作
        this.menuItemSetOnAction();
    }

    // 最小化窗口
    public static void minimizeOnShowApp(boolean isMinimize) {
        if (fileAppAdditionStageList == null) return;
        for (int i = 0; i < fileAppAdditionStageList.size(); ++i) {
            Stage stage = fileAppAdditionStageList.get(i);
            if (stage == null) continue;
            if (!stage.isShowing()) fileAppAdditionStageList.remove(stage);
            else stage.setIconified(isMinimize);
        }
    }

    // 初始化右键菜单
    private void menuInit() {
        // 创建右键菜单项
        this.createFileItem = new MenuItem("新建文件");
        this.createFolderItem = new MenuItem("新建文件夹");
        this.pasteItem = new MenuItem("粘贴");
        this.openItem = new MenuItem("打开");
        this.delItem = new MenuItem("删除");
        this.propItem = new MenuItem("属性");
        this.copyItem = new MenuItem("复制");
        this.moveItem = new MenuItem("剪切");

        // 创建文件管理器的右键菜单
        this.contextMenu1 = new ContextMenu(this.createFileItem, this.createFolderItem, this.pasteItem);
        // 创建文件/目录的右键菜单
        this.contextMenu2 = new ContextMenu(this.openItem, this.delItem, this.propItem, this.copyItem, this.moveItem);

        // 处理文件管理器面板的右键点击事件
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

    // 初始化表格
    private void tableInit() {
        // 获取“磁盘使用”表格中的列
        var disk = controller.diskTable.getColumns().get(0);
        var index = controller.diskTable.getColumns().get(1);
        var type = controller.diskTable.getColumns().get(2);
        var content = controller.diskTable.getColumns().get(3);

        // 获取“已打开文件”表格中的列
        var fileName = controller.openFile.getColumns().get(0);
        var openType = controller.openFile.getColumns().get(1);
        var beginDisk = controller.openFile.getColumns().get(2);
        var fileLength = controller.openFile.getColumns().get(3);
        var filePath = controller.openFile.getColumns().get(4);

        // 为“磁盘使用”表格列设置数据源
        disk.setCellValueFactory(new PropertyValueFactory<>("numP"));
        index.setCellValueFactory(new PropertyValueFactory<>("indexP"));
        type.setCellValueFactory(new PropertyValueFactory<>("typeP"));
        content.setCellValueFactory(new PropertyValueFactory<>("objectP"));

        // 为“已打开文件”表格列设置数据源
        fileName.setCellValueFactory(new PropertyValueFactory<>("NameP"));
        openType.setCellValueFactory(new PropertyValueFactory<>("flagP"));
        beginDisk.setCellValueFactory(new PropertyValueFactory<>("diskNumP"));
        fileLength.setCellValueFactory(new PropertyValueFactory<>("lengthP"));
        filePath.setCellValueFactory(new PropertyValueFactory<>("locationP"));

        // 创建用于显示磁盘块信息的数据列表并绑定到表格视图
        ObservableList<Disk> disksItem = FXCollections.observableArrayList(fat.getDiskBlocks());
        controller.diskTable.setItems(disksItem);

        // 创建用于显示已打开文件信息的数据列表并绑定到表格视图
        ObservableList<File> fileOpened = fat.getOpenedFiles();
        controller.openFile.setItems(fileOpened);
    }

    // 初始化树视图
    private void treeViewInit() {
        // 获取磁盘图标的URL
        URL location = this.getClass().getResource("/com/os/applications/fileApp/res/disk.png");
        // 创建根节点，并使用磁盘图标作为根节点的图形
        TreeItem<String> rootNode = new TreeItem<>("C:", new ImageView(String.valueOf(location)));
        // 设置根节点图标的大小
        CompSet.setImageViewFixSize((ImageView) rootNode.getGraphic(), 20, 20);

        // 展开根节点
        rootNode.setExpanded(true);
        // 将根节点设置为当前节点
        this.currentNode = rootNode;
        // 将根节点关联到文件系统路径 "C:"
        this.pathMap.put(fat.getPath("C:"), rootNode);
        // 将根节点设置为树视图的根节点
        controller.treeView.setRoot(rootNode);
        controller.treeView.setCellFactory((p) -> new TextFieldTreeCellImpl());

        // 遍历文件系统中的路径，初始化树视图
        for (Path path : fat.getPaths()) {
            if (path.hasParent() && path.getParent().getPathName().equals(rootNode.getValue())) {
                this.TreeNodeInit(path, rootNode);
            }
        }

        // 添加图标以显示文件夹和文件
        this.addIcon(fat.getBlockList(this.currentPath));
    }

    // 初始化树视图节点
    private void TreeNodeInit(Path newPath, TreeItem<String> parentNode) {
        TreeItem<String> newNode = this.addNode(parentNode, newPath);
        if (!newPath.hasChild()) return;
        for (Path child : newPath.getChildren())
            this.TreeNodeInit(child, newNode);
    }

    // 添加树视图节点
    private TreeItem<String> addNode(TreeItem<String> parentNode, Path newPath) {
        // 获取新节点的路径名称
        String pathName = newPath.getPathName();
        // 从路径名称中提取节点的名称（即最后一个路径分隔符后的部分）
        String value = pathName.substring(pathName.lastIndexOf(92) + 1);

        // 创建新节点，设置节点的名称和图标
        URL location = this.getClass().getResource("/com/os/applications/fileApp/res/node.png");
        TreeItem<String> newNode = new TreeItem<>(value, new ImageView(String.valueOf(location)));

        // 设置新节点为展开状态
        newNode.setExpanded(true);
        // 将新节点与对应的路径对象关联
        this.pathMap.put(newPath, newNode);

        // 调整新节点图标的大小
        ((ImageView) newNode.getGraphic()).setFitWidth(15.0);
        ((ImageView) newNode.getGraphic()).setFitHeight(15.0);

        // 将新节点添加到父节点的子节点列表中
        parentNode.getChildren().add(newNode);

        return newNode;
    }

    // 移除树视图节点
    public void removeNode(TreeItem<String> recentNode, Path remPath) {
        recentNode.getChildren().remove(this.pathMap.get(remPath));
        this.pathMap.remove(remPath);
    }

    // 添加图标
    public void addIcon(List<Disk> diskList) {
        // 将文件或文件夹列表保存到成员变量中
        this.blockList = diskList;
        final int n = diskList.size();
        this.icons = new Label[n];

        for (int i = 0; i < n; ++i) {
            String fName;
            // 根据对象类型（文件夹或文件）设置标签的文本和图标
            if (diskList.get(i).getObject() instanceof Folder) {
                fName = ((Folder) diskList.get(i).getObject()).getName();
                if (fName.length() > 4) fName = fName.substring(0, 4) + "..";
                // 获取文件夹图标的URL
                URL location = this.getClass().getResource("/com/os/applications/fileApp/res/folder.png");
                this.icons[i] = new Label(fName, new ImageView(String.valueOf(location)));
            } else {
                fName = ((File) diskList.get(i).getObject()).getName();
                if (fName.length() > 4) fName = fName.substring(0, 4) + "..";
                // 获取文件图标的URL
                URL location = this.getClass().getResource("/com/os/applications/fileApp/res/file.png");
                this.icons[i] = new Label(fName, new ImageView(String.valueOf(location)));
            }
            // 设置标签的大小和对齐方式
            this.icons[i].setPrefSize(100.0, 100.0);
            this.icons[i].setMinSize(100.0, 100.0);
            this.icons[i].setMaxSize(100.0, 100.0);
            this.icons[i].setAlignment(Pos.CENTER);
            ((ImageView) this.icons[i].getGraphic()).setFitWidth(60.0);
            ((ImageView) this.icons[i].getGraphic()).setFitHeight(60.0);
            this.icons[i].setContentDisplay(ContentDisplay.TOP);
            this.icons[i].setWrapText(true);

            // 将标签添加到 flowPane
            controller.flowPane.getChildren().add(this.icons[i]);

            // 设置鼠标悬停事件处理程序
            this.icons[i].setOnMouseEntered(event -> {
                // 获取触发事件的标签
                Label src = (Label) event.getSource();
                // 遍历标签数组，找到触发事件的标签的索引
                for (int j = 0; j < n; ++j)
                    if (src == FileApplication.this.icons[j]) FileApplication.this.currentId = j;

                // 获取与该标签关联的磁盘块对象
                Disk thisBlock = FileApplication.this.blockList.get(FileApplication.this.currentId);
                // 更改背景颜色
                Tooltip.install(controller.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: rgb(103, 157, 238, 0.5); " +
                        "-fx-background-radius: 12;");
            });

            // 设置鼠标离开事件处理程序，还原背景颜色
            this.icons[i].setOnMouseExited(event -> {
                // 获取与该标签关联的磁盘块对象
                Disk thisBlock = FileApplication.this.blockList.get(FileApplication.this.currentId);
                // 还原背景颜色
                Tooltip.uninstall(controller.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: transparent;");
            });

            // 设置鼠标单击事件处理程序
            this.icons[i].setOnMouseClicked(event -> {
                // 获取触发事件的标签
                Label src = (Label) event.getSource();
                // 遍历标签数组，找到触发事件的标签的索引
                for (int j = 0; j < n; ++j)
                    if (src == FileApplication.this.icons[j]) FileApplication.this.currentId = j;

                // 启用复制、移动和创建文件选项
                FileApplication.this.copyItem.setDisable(false);
                FileApplication.this.moveItem.setDisable(false);
                FileApplication.this.createFileItem.setDisable(false);

                // 右键单击处理上下文菜单
                if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                    Disk thisBlock = FileApplication.this.blockList.get(FileApplication.this.currentId);
                    // 如果是文件夹，禁用复制和移动选项
                    if (thisBlock.getType().equals("文件夹")) {
                        FileApplication.this.copyItem.setDisable(true);
                        FileApplication.this.moveItem.setDisable(true);
                    }
                    // 在鼠标右键单击位置显示上下文菜单
                    FileApplication.this.contextMenu2.show(src, event.getScreenX(), event.getScreenY());
                }
                // 左键双击处理打开操作
                else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    try {
                        FileApplication.this.onOpen();
                    } catch (IOException e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                }
                // 隐藏上下文菜单
                else FileApplication.this.contextMenu2.hide();
            });
        }
    }

    // 打开文件/文件夹
    private void onOpen() throws IOException {
        // 获取当前选中的磁盘块
        Disk thisBlock = this.blockList.get(this.currentId);

        // 判断是否是一个文件
        if (thisBlock.getObject() instanceof File) {
            if (fat.getOpenedFiles().size() < 5) {
                // 查看文件内容
                try {
                    fileViewOpen((File) thisBlock.getObject(), thisBlock);
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
                // 如果文件未打开，标记为打开
                if (!fat.isOpenedFile(thisBlock)) fat.addOpenedFile(thisBlock);
            }
            // 打开文件数量已达上限（5），提示用户
            else {
                try {
                    tipOpen("文件打开已到上限!");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        }
        // 如果选中的是文件夹
        else {
            // 获取选中文件夹和对应的路径
            Folder thisFolder = (Folder) thisBlock.getObject();
            String newPath = thisFolder.getLocation() + "\\" + thisFolder.getName();

            // 清空 flowPane
            controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
            // 添加新路径下的新图标
            this.addIcon(fat.getBlockList(newPath));

            // 更新当前路径和当前节点
            controller.currentPath.setText(newPath);
            this.currentPath = newPath;
            this.currentNode = this.pathMap.get(thisFolder.getPath());

            // 展开当前节点
            this.currentNode.setExpanded(true);
            // 选中当前节点
            this.controller.treeView.getSelectionModel().select(currentNode);
        }
    }

    // 获取当前节点
    public TreeItem<String> getCurrentNode() {
        return this.currentNode;
    }

    // 设置右键菜单项的操作
    private void menuItemSetOnAction() {
        // 创建文件菜单项的操作
        this.createFileItem.setOnAction((ActionEvent) -> {
            if (((Folder) fat.getDiskBlocks()[2].getObject()).getCatalogNum() > 7
                    && controller.currentPath.getText().equals("C:")) {
                try {
                    tipOpen("根路径最多创建\n8个目录项!");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            } else {
                int no = fat.createFile(this.currentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法创建!");
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    // 清空图标区域并显示更新后的图标
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.currentPath));
                }
            }
        });

        // 创建文件夹菜单项的操作
        this.createFolderItem.setOnAction((ActionEvent) -> {
            if (((Folder) fat.getDiskBlocks()[2].getObject()).getCatalogNum() > 7
                    && controller.currentPath.getText().equals("C:")) {
                try {
                    tipOpen("根路径最多创建\n8个目录项!");
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            } else {
                int no = fat.createFolder(this.currentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法创建!");
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    Folder newFolder = (Folder) fat.getBlock(no).getObject();
                    Path newPath = newFolder.getPath();
                    // 清空图标区域并显示更新后的图标
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.currentPath));
                    // 添加新的树视图节点
                    this.addNode(this.currentNode, newPath);
                }
            }
        });

        // 打开菜单项的操作
        this.openItem.setOnAction((ActionEvent) -> {
            try {
                this.onOpen();
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        });

        // 删除菜单项的操作
        this.delItem.setOnAction((ActionEvent) -> {
            // 获取当前选中的磁盘块
            Disk thisBlock = this.blockList.get(this.currentId);
            if (fat.isOpenedFile(thisBlock)) {
                try {
                    tipOpen("文件未关闭!");
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

        // 属性菜单项的操作
        this.propItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.currentId);
            try {
                this.propertyOpen(thisBlock, this.icons[this.currentId], this.pathMap);
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        });

        // 复制菜单项的操作
        this.copyItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.currentId);
            copyBlock = thisBlock;
            copyFile = (File) thisBlock.getObject();
            copyFlag = true;
        });

        // 剪切菜单项的操作
        this.moveItem.setOnAction((ActionEvent) -> {
            Disk thisBlock = this.blockList.get(this.currentId);
            copyBlock = thisBlock;
            copyFile = (File) thisBlock.getObject();
            copyFlag = true;
            moveFlag = true;
        });

        // 粘贴菜单项的操作
        this.pasteItem.setOnAction((ActionEvent) -> {
            if (copyFlag) {
                int no = fat.createFile(this.currentPath);
                if (no == -1) {
                    try {
                        tipOpen("磁盘容量已满，无法粘贴!");
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    if (moveFlag) {
                        fat.delete(copyBlock);
                        moveFlag = false;
                    }
                    // 清空图标区域并显示更新后的图标
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.currentPath));
                }
                copyFlag = false;
            }
        });
    }

    // 打开属性窗口
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

    // 打开提示窗口
    public static void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipDialogApplication tipWindow = new TipDialogApplication(tipString);
        tipWindow.start(stage);

        Text text = new Text(tipString);
        text.setFill(Color.RED);
        text.setFont(javafx.scene.text.Font.font("宋体", 25.0D));
        tipWindow.controller.tipTextFlow.getChildren().add(text);
    }

    // 打开文件查看窗口
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

    // 打开删除文件/目录窗口
    public void delViewOpen(Disk block) throws Exception {
        Stage stage = new Stage();
        DeleteDialogApplication deleteDialogApplication = new DeleteDialogApplication(this, block);
        deleteDialogApplication.start(stage);
    }

    // 加载数据
    public static void loadData() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./data"))) {
            fat = (FAT) inputStream.readObject();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        // 如果没有 fat，则新建 fat
        if (fat == null) fat = new FAT();
    }

    // 保存数据
    public static void saveData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data"))) {
            outputStream.writeObject(fat);
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void clearData() {
        fat = new FAT();

        controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());
        this.currentPath = "C:";
        // 关闭所有FAT
        FAT.closeAll();
        // 初始化树视图
        this.treeViewInit();
        // 初始化表格
        this.tableInit();
    }

    public final class TextFieldTreeCellImpl extends TreeCell<String> {
        private final TextField textField = new TextField();  // 用于编辑单元格文本的文本字段

        public TextFieldTreeCellImpl() {
            // 鼠标单击事件的处理
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1
                        && TextFieldTreeCellImpl.this.getTreeItem() != null) {
                    String pathName = null;

                    // 查找与单击的树节点关联的路径名称
                    for (Map.Entry<Path, TreeItem<String>> pathTreeItemEntry : FileApplication.this.pathMap.entrySet()) {
                        if (TextFieldTreeCellImpl.this.getTreeItem() == pathTreeItemEntry.getValue()) {
                            pathName = pathTreeItemEntry.getKey().getPathName();
                            break;
                        }
                    }

                    // 清空 flowPane
                    controller.flowPane.getChildren().removeAll(controller.flowPane.getChildren());

                    // 根据路径名称获取磁盘块列表
                    List<Disk> fats = FileApplication.fat.getBlockList(pathName);
                    // 根据路径添加新图标
                    FileApplication.this.addIcon(fats);

                    // 更新当前路径和当前节点
                    FileApplication.this.currentPath = pathName;
                    FileApplication.this.currentNode = TextFieldTreeCellImpl.this.getTreeItem();
                    // 更新当前路径
                    controller.currentPath.setText(FileApplication.this.currentPath);
                }
            });
        }

        // 更新单元格内容
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            // 如果单元格为空，清空文本内容和图形
            if (empty) {
                this.setText(null);
                this.setGraphic(null);
            } else if (this.isEditing()) {
                this.textField.setText(this.getString());
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
