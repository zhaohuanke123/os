package com.os.apps.fileApp.app;

import com.os.apps.fileApp.Controller.MainCtl;
import com.os.utils.fileSystem.*;
import com.os.utils.fileSystem.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class MainUI {
    private TreeItem<String> rootNode;
    private TreeItem<String> recentNode;
    public static FAT fat;
    private int ind;
    public static Disk copyBlock;
    private List<Disk> blockList;
    public String recentPath;
    public static File copyFile;
    public static boolean copyFlag;
    public static boolean moveFlag = false;
    private final Map<Path, TreeItem<String>> pathMap = new HashMap<>();
    private ObservableList<Disk> disksItem;
    private ObservableList<File> fileOpened;
    private ContextMenu contextMenu;
    private ContextMenu contextMenu2;
    private MenuItem createFileItem;
    private MenuItem createFolderItem;
    private MenuItem openItem;
    private MenuItem delItem;
    private MenuItem propItem;
    private MenuItem copyItem;
    private MenuItem moveItem;
    private MenuItem pasteItem;
    private Label[] icons;
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/os/apps/fileApp/fxmls/mainUI.fxml"));
    private final Parent root;
    public FlowPane flowPane;
    private final Label currentPath;
    private final TreeView treeView;
    private final TabPane TabP;
    private final Tab chartTab;
    public static boolean clearFlag = false;
    public static Vector<Stage> fileAppAdditionStageList = new Vector<>();

    public static void updateFileStageList(Stage stage) {
        for (int i = 0; i < fileAppAdditionStageList.size(); ++i) {
            if (fileAppAdditionStageList.get(i) == stage) {
                fileAppAdditionStageList.remove(stage);
                break;
            }
        }

        fileAppAdditionStageList.add(stage);
    }

    public MainUI(Stage primaryStage) throws Exception {
        this.root = this.fxmlLoader.load();
        this.flowPane = (FlowPane) this.root.lookup("#flowPane");
        this.currentPath = (Label) this.root.lookup("#currentPath");
        this.treeView = (TreeView) this.root.lookup("#treeView");
        this.TabP = (TabPane) this.root.lookup("#TabP");
        this.chartTab = this.TabP.getTabs().get(0);
        new LoadView();
        Scene scene;
        primaryStage.setResizable(false);
        URL location = this.getClass().getResource("/com/os/apps/fileApp/res/folder.png");
        primaryStage.getIcons().add(new Image(String.valueOf(location)));
        primaryStage.setTitle("磁盘文件管理系统");
        scene = new Scene(this.root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        MainCtl MainCtl = this.fxmlLoader.getController();
        MainCtl.init(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        this.recentPath = "C:";
        primaryStage.setOnCloseRequest((e) -> {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data"));
                Throwable var2 = null;

                try {
                    System.out.println("saving");
                    outputStream.writeObject(fat);
                } catch (Throwable var13) {
                    var2 = var13;
                    throw var13;
                } finally {
                    if (var2 != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable var12) {
                            var2.addSuppressed(var12);
                        }
                    } else {
                        outputStream.close();
                    }

                }
            } catch (IOException var16) {
                System.out.println(Arrays.toString(var16.getStackTrace()));
            }

        });
        this.loadData();
        FAT.closeAll();
        this.menuInit();
        this.menuItemSetOnAction();
        this.initTreeView();
        this.tableInit();
        primaryStage.show();
        this.chartTab.setOnSelectionChanged((ActionEvent) -> this.pieInit());
    }

    private void loadData() {
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
        } catch (Exception var14) {
            System.out.println("读取完毕");
            System.out.println("clearFlag=" + clearFlag);
        }

        if (fat == null || clearFlag) {
            fat = new FAT();
            clearFlag = false;
            System.out.println("fat=null");
        }

    }

    private void menuInit() {
        this.createFileItem = new MenuItem("新建文件");
        this.createFolderItem = new MenuItem("新建文件夹");
        this.pasteItem = new MenuItem("粘贴");
        this.openItem = new MenuItem("打开");
        this.delItem = new MenuItem("删除");
        this.propItem = new MenuItem("属性");
        this.copyItem = new MenuItem("复制");
        this.moveItem = new MenuItem("剪切");
        this.contextMenu = new ContextMenu(this.createFileItem, this.createFolderItem, this.pasteItem);
        this.contextMenu2 = new ContextMenu(this.openItem, this.delItem, this.propItem, this.copyItem, this.moveItem);
        this.flowPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (me) -> {
            if (me.getButton() == MouseButton.SECONDARY && !this.contextMenu2.isShowing()) {
                this.pasteItem.setDisable(true);
                if (copyFlag || moveFlag) {
                    this.pasteItem.setDisable(false);
                }

                this.contextMenu.show(this.flowPane, me.getScreenX(), me.getScreenY());
            } else {
                this.contextMenu.hide();
            }

        });
    }

    private void tableInit() {
        TableView<Disk> diskTable = (TableView) this.root.lookup("#diskTable");
        TableColumn<Disk, String> disk = (TableColumn) diskTable.getColumns().get(0);
        TableColumn<Disk, String> index = (TableColumn) diskTable.getColumns().get(1);
        TableColumn<Disk, String> type = (TableColumn) diskTable.getColumns().get(2);
        TableColumn<Disk, String> content = (TableColumn) diskTable.getColumns().get(3);
        TableView<File> openFile = (TableView) this.root.lookup("#openFile");
        TableColumn<File, String> fileName = (TableColumn) openFile.getColumns().get(0);
        TableColumn<File, String> openType = (TableColumn) openFile.getColumns().get(1);
        TableColumn<File, String> beginDisk = (TableColumn) openFile.getColumns().get(2);
        TableColumn<File, String> fileLength = (TableColumn) openFile.getColumns().get(3);
        TableColumn<File, String> filePath = (TableColumn) openFile.getColumns().get(4);
        disk.setCellValueFactory(new PropertyValueFactory<>("numP"));
        index.setCellValueFactory(new PropertyValueFactory<>("indexP"));
        type.setCellValueFactory(new PropertyValueFactory<>("typeP"));
        content.setCellValueFactory(new PropertyValueFactory<>("objectP"));
        this.disksItem = FXCollections.observableArrayList(fat.getDiskBlocks());
        diskTable.setItems(this.disksItem);
        fileName.setCellValueFactory(new PropertyValueFactory<>("fileNameP"));
        openType.setCellValueFactory(new PropertyValueFactory<>("flagP"));
        beginDisk.setCellValueFactory(new PropertyValueFactory<>("diskNumP"));
        fileLength.setCellValueFactory(new PropertyValueFactory<>("lengthP"));
        filePath.setCellValueFactory(new PropertyValueFactory<>("locationP"));
        this.fileOpened = fat.getOpenedFiles();
        openFile.setItems(this.fileOpened);
    }

    public void pieInit() {
        int UsedNum = 0;

        for (int i = 0; i < 256; ++i) {
            if (fat.getDiskBlocks()[i].getIndex() != 0) {
                ++UsedNum;
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("##.00%");
        System.out.println(decimalFormat.format(UsedNum / 256));
        System.out.println("已占用：" + UsedNum);
        PieChart pieChart = (PieChart) this.root.lookup("#pie");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new PieChart.Data("未占用  " + decimalFormat.format((double) (256 - UsedNum) / 256.0), 256 - UsedNum), new PieChart.Data("已占用  " + decimalFormat.format((double) UsedNum / 256.0), UsedNum));
        pieChart.setData(pieChartData);
        pieChart.setLabelsVisible(false);
    }

    private void initTreeView() {
        URL location = this.getClass().getResource("/com/os/apps/fileApp/res/disk.png");
        this.rootNode = new TreeItem<>("C:", new ImageView(String.valueOf(location)));
        ((ImageView) this.rootNode.getGraphic()).setFitWidth(20.0);
        ((ImageView) this.rootNode.getGraphic()).setFitHeight(20.0);
        this.rootNode.setExpanded(true);
        this.recentNode = this.rootNode;
        this.pathMap.put(fat.getPath("C:"), this.rootNode);
        this.treeView.setRoot(this.rootNode);
        this.treeView.setCellFactory((p) -> new TextFieldTreeCellImpl());

        for (Path path : fat.getPaths()) {
            System.out.println(path);
            if (path.hasParent() && path.getParent().getPathName().equals(this.rootNode.getValue())) {
                this.initTreeNode(path, this.rootNode);
            }
        }

        this.addIcon(fat.getBlockList(this.recentPath), this.recentPath);
    }

    private void initTreeNode(Path newPath, TreeItem<String> parentNode) {
        TreeItem<String> newNode = this.addNode(parentNode, newPath);
        if (newPath.hasChild()) {

            for (Path child : newPath.getChildren()) {
                this.initTreeNode(child, newNode);
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
                nName = ((Folder) bList.get(i).getObject()).getFolderName();
                if (nName.length() > 4) {
                    nName = nName.substring(0, 4) + "..";
                }

                URL location = this.getClass().getResource("/com/os/apps/fileApp/res/folder.png");
                this.icons[i] = new Label(nName, new ImageView(String.valueOf(location)));
            } else {
                nName = ((File) bList.get(i).getObject()).getFileName();
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
            this.flowPane.getChildren().add(this.icons[i]);
            this.icons[i].setOnMouseEntered(event -> {
                Label src = (Label) event.getSource();

                for (int j = 0; j < n; ++j) {
                    if (src == MainUI.this.icons[j]) {
                        MainUI.this.ind = j;
                    }
                }

                Disk thisBlock = MainUI.this.blockList.get(MainUI.this.ind);
                Tooltip.install(MainUI.this.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: rgba(240,248,255,0.5);");
            });
            this.icons[i].setOnMouseExited(event -> {
                Disk thisBlock = MainUI.this.blockList.get(MainUI.this.ind);
                Tooltip.uninstall(MainUI.this.flowPane, new Tooltip(thisBlock.getObject().toString()));
                ((Label) event.getSource()).setStyle("-fx-background-color: rgba(240,248,255,0);");
            });
            this.icons[i].setOnMouseClicked(event -> {
                Label src = (Label) event.getSource();

                for (int j = 0; j < n; ++j) {
                    if (src == MainUI.this.icons[j]) {
                        MainUI.this.ind = j;
                    }
                }

                MainUI.this.copyItem.setDisable(false);
                MainUI.this.moveItem.setDisable(false);
                MainUI.this.createFileItem.setDisable(false);
                if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                    Disk thisBlock = MainUI.this.blockList.get(MainUI.this.ind);
                    if (thisBlock.getType().equals("文件夹")) {
                        MainUI.this.copyItem.setDisable(true);
                        MainUI.this.moveItem.setDisable(true);
                    }

                    MainUI.this.contextMenu2.show(src, event.getScreenX(), event.getScreenY());
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    try {
                        MainUI.this.onOpen();
                    } catch (IOException var4) {
                        System.out.println(Arrays.toString(var4.getStackTrace()));
                        System.out.println(Arrays.toString(var4.getStackTrace()));
                    }
                } else {
                    MainUI.this.contextMenu2.hide();
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
                        System.out.println(Arrays.toString(var5.getStackTrace()));
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
            String newPath = thisFolder.getLocation() + "\\" + thisFolder.getFolderName();
            this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
            this.addIcon(fat.getBlockList(newPath), newPath);
            this.currentPath.setText(newPath);
            this.recentPath = newPath;
            this.recentNode = this.pathMap.get(thisFolder.getPath());
        }

    }

    public TreeItem<String> getRecentNode() {
        return this.recentNode;
    }

    private void menuItemSetOnAction() {
        this.createFileItem.setOnAction((ActionEvent) -> {
            if (((Folder) fat.getDiskBlocks()[2].getObject()).getCatalogNum() > 7 && this.currentPath.getText().equals("C:")) {
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
                    this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
                    this.addIcon(fat.getBlockList(this.recentPath), this.recentPath);
                }

            }
        });
        this.createFolderItem.setOnAction((ActionEvent) -> {
            if (((Folder) fat.getDiskBlocks()[2].getObject()).getCatalogNum() > 7 && this.currentPath.getText().equals("C:")) {
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
                    this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
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

                    this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
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
            propertyView = new PropertyView(thisBlock, icon, pathMap, stage);
        } catch (IOException var7) {
            System.out.println(Arrays.toString(var7.getStackTrace()));
        }

        if (propertyView != null) {
            propertyView.start(stage);
        }
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
    }

    public static void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipWindow tipWindow = new TipWindow(tipString);
        tipWindow.start(stage);
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        fileAppAdditionStageList.add(stage);
    }

    public static void fileViewOpen(File file, Disk block) throws Exception {
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
            fileView.start(stage);
            System.out.println("fileViewOpen" + file.isOpened());
            stage.setAlwaysOnTop(true);
            stage.setIconified(false);
            stage.toFront();
            fileAppAdditionStageList.add(stage);
        }

    }

    public void delViewOpen(Disk block) throws Exception {
        Stage stage = new Stage();
        DelView delView = new DelView(this, block);
        fileAppAdditionStageList.add(stage);
        delView.start(stage);
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
    }

    public static void saveData() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data"));
            Throwable var1 = null;

            try {
                System.out.println("saving");
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

                    for (Map.Entry<Path, TreeItem<String>> pathTreeItemEntry : MainUI.this.pathMap.entrySet()) {
                        if (TextFieldTreeCellImpl.this.getTreeItem() == pathTreeItemEntry.getValue()) {
                            pathName = pathTreeItemEntry.getKey().getPathName();
                            break;
                        }
                    }

                    List<Disk> fats = MainUI.fat.getBlockList(pathName);
                    MainUI.this.flowPane.getChildren().removeAll(MainUI.this.flowPane.getChildren());
                    MainUI.this.addIcon(fats, pathName);
                    MainUI.this.recentPath = pathName;
                    System.out.println(pathName);
                    MainUI.this.recentNode = TextFieldTreeCellImpl.this.getTreeItem();
                    MainUI.this.currentPath.setText(MainUI.this.recentPath);
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
