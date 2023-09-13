package com.os.main;

import com.os.apps.fileApp.app.MainUI;
import com.os.apps.helpApp.HelpApp;
import com.os.apps.occupancyApp.OccupancyApp;
import com.os.apps.processApp.ProcessApp;
import com.os.apps.systemFileApp.SystemFileApp;
import com.os.utils.fileSystem.FAT;
import com.os.utils.process.OccupancyManager;
import com.os.utils.process.ProcessManager;
import com.os.utils.process.ProcessScheduleThread;
import com.os.utils.ui.StageRecord;
import com.os.utils.ui.UIThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class MainController {
    public ProcessScheduleThread processScheduleThread = new ProcessScheduleThread();
    public UIThread uiThread = new UIThread();
    public Pane buttonBarBackGround;
    Scene scene = null;
    Stage primaryStage = null;
    public static Vector<StageRecord> stageList = new Vector<>();
    double appWidth;
    double sceneWidth;
    double sceneHeight;
    @FXML
    private Pane MainWindow;
    @FXML
    private ImageView background;
    @FXML
    private Pane buttonBar;
    @FXML
    private HBox appBox;
    @FXML
    private Button systemFileButton;
    @FXML
    private Button processButton;
    @FXML
    private Button occupancyButton;
    @FXML
    private HBox tipBox;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button closeButton;
    @FXML
    private VBox timeBox;
    @FXML
    private Button timeButton1;
    @FXML
    private Button timeButton2;
    @FXML
    private Button deskButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button fileManagerButton;
    boolean isTop = false;
    boolean haveChanged = true;
    private final String buttonStyle =
            "-fx-background-color: transparent,aliceblue;" +
            "-fx-background-radius: 12;" +
            "-fx-text-fill: black;" +
            "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );";

    @FXML
    void closeWindow(MouseEvent event) {
        FAT.closeAll();
        if (MainUI.fat != null) {
            MainUI.saveData();
        }

        System.exit(0);
        Platform.exit();
        this.primaryStage.close();
    }

    @FXML
    void minimizeWindow(MouseEvent event) {
        int i;
        for (i = 0; i < stageList.size(); ++i) {
            StageRecord stageRecord = stageList.get(i);
            if (stageRecord.name.contains("com/os/apps/")) {
                Stage stage = stageRecord.stage;
                if (stage != null) {
                    stage.setIconified(true);
                }
            }
        }

        if (MainUI.fileAppAdditionStageList != null) {
            for (i = 0; i < MainUI.fileAppAdditionStageList.size(); ++i) {
                Stage stage1 = MainUI.fileAppAdditionStageList.get(i);
                if (stage1 != null && !stage1.isShowing()) {
                    MainUI.fileAppAdditionStageList.remove(stage1);
                } else {
                    if (stage1 != null) {
                        stage1.setIconified(true);
                    }
                }
            }
        }

        this.primaryStage.setIconified(true);
    }

    public void iconInit() {
        this.appWidth = this.scene.getHeight() / 15.0;
        this.sceneWidth = this.scene.getWidth();
        this.sceneHeight = this.scene.getHeight();

        //
        this.background.setLayoutX(0.0);
        this.background.setLayoutY(0.0);
        this.background.fitWidthProperty().bind(this.scene.widthProperty());
        this.background.fitHeightProperty().bind(this.scene.heightProperty());
        this.background.setPreserveRatio(false);
        this.background.setVisible(true);
        //

        this.systemFileButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                System.out.println("executableFileApp open success");
                MainController.this.systemFileAppOpen();
            }

        });
        this.processButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                System.out.println("processApp open success");
                MainController.this.processAppOpen();
            }

        });
        this.occupancyButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                System.out.println("occupancyApp open success");
                MainController.this.occupancyAppOpen();
            }

        });
        this.helpButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                System.out.println("helpApp open success");
                MainController.this.helpAppOpen();
            }

        });
        this.deskButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                MainController.this.toDesk();
            }

        });
        this.fileManagerButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                System.out.println("fileApp open success");

                try {
                    MainController.this.fileAppOpen();
                } catch (Exception var3) {
                    System.out.println(Arrays.toString(var3.getStackTrace()));
                }
            }

        });

        Button[] buttons = new Button[]{this.systemFileButton,
                this.fileManagerButton, this.processButton,
                this.occupancyButton, this.helpButton};

        String tooltipStyle = "-fx-font-size: 12";
        Tooltip tooltip = new Tooltip("系统文件表");
        tooltip.setStyle(tooltipStyle);
        this.systemFileButton.setTooltip(tooltip);
        tooltip = new Tooltip("文件管理器");
        tooltip.setStyle(tooltipStyle);
        this.fileManagerButton.setTooltip(tooltip);
        tooltip = new Tooltip("进程管理器");
        tooltip.setStyle(tooltipStyle);
        this.processButton.setTooltip(tooltip);
        tooltip = new Tooltip("占用管理器");
        tooltip.setStyle(tooltipStyle);
        this.occupancyButton.setTooltip(tooltip);
        tooltip = new Tooltip("帮助");
        tooltip.setStyle(tooltipStyle);
        this.helpButton.setTooltip(tooltip);
        tooltip = new Tooltip("最小化");
        tooltip.setStyle(tooltipStyle);
        this.minimizeButton.setTooltip(tooltip);
        tooltip = new Tooltip("关闭");
        tooltip.setStyle(tooltipStyle);
        this.closeButton.setTooltip(tooltip);
        tooltip = new Tooltip("显示桌面");
        tooltip.setStyle(tooltipStyle);
        this.deskButton.setTooltip(tooltip);

        UIThread.mainButtons = buttons;
        UIThread.stageList = stageList;
    }

    // 返回桌面
    public void toDesk() {
        if (this.haveChanged) {
            this.haveChanged = false;

            if (!this.isTop) {
                this.primaryStage.toFront();

                for (StageRecord stageRecord : stageList) {
                    Stage stage1 = stageRecord.stage;
                    if (stage1 != null && stage1.isShowing()) {
                        stage1.setIconified(true);
                    }
                }

                if (MainUI.fileAppAdditionStageList != null) {
                    for (int i = 0; i < MainUI.fileAppAdditionStageList.size(); ++i) {
                        Stage stage1 = MainUI.fileAppAdditionStageList.get(i);

                        if (stage1 != null && !stage1.isShowing()) {
                            MainUI.fileAppAdditionStageList.remove(stage1);
                        } else {
                            if (stage1 != null) {
                                stage1.setIconified(true);
                            }
                        }
                    }
                }
            } else {
                for (StageRecord stageRecord : stageList) {
                    Stage stage1 = stageRecord.stage;
                    if (stage1 != null && stage1.isShowing()) {
                        stage1.setIconified(false);
                    }
                }

                if (MainUI.fileAppAdditionStageList != null) {
                    for (int i = 0; i < MainUI.fileAppAdditionStageList.size(); ++i) {
                        Stage stage1 = MainUI.fileAppAdditionStageList.get(i);

                        if (stage1 != null && !stage1.isShowing()) {
                            MainUI.fileAppAdditionStageList.remove(stage1);
                        } else {
                            if (stage1 != null) {
                                stage1.setIconified(false);
                            }
                        }
                    }
                }
            }

            this.isTop = !this.isTop;
            this.haveChanged = !this.haveChanged;
        }

    }

    public void init(Scene scene, Stage stage) throws URISyntaxException {
        this.scene = scene;
        this.primaryStage = stage;
        this.primaryStage.setOnCloseRequest(event -> {
            System.out.println("结束");
            System.exit(0);
        });
        this.iconInit();
        this.timeInit();
        OccupancyManager.init();
        this.processThreadInit();
        this.uiThreadInit();
    }

    // 设置按钮的大小
    private void setCompSize(Button button, double width, double height) {
        button.setPrefSize(width, height);
        button.setMinSize(width, height);
        button.setMaxSize(width, height);
        button.resize(width, height);
    }

    // 设置图像视图的宽度和高度
    private void setImageViewSize(ImageView button, double width, double height) {
        button.setFitWidth(width);
        button.setFitHeight(height);
    }

    public void adaptWindow() {
        this.MainWindow.setPrefSize(this.scene.getWidth(), this.scene.getHeight());
        System.out.println("MainWindow:" + this.MainWindow.getWidth() + "," + this.MainWindow.getHeight());
        // 初始化任务栏
        this.buttonBar.setMaxHeight(1 * this.appWidth);
        this.buttonBar.setMinHeight(1 * this.appWidth);
        this.buttonBar.setPrefHeight(1 * this.appWidth);
        this.buttonBar.setMaxWidth(this.sceneWidth);
        this.buttonBar.setMinWidth(this.sceneWidth);
        this.buttonBar.setPrefWidth(this.sceneWidth);
        this.buttonBar.setLayoutX(0);
        this.buttonBar.setLayoutY(this.sceneHeight - 1 * this.appWidth);

        // 初始化任务栏背景 ------------------------------------------------------------
        this.buttonBarBackGround.setMinHeight(1 * this.appWidth);
        this.buttonBarBackGround.setMaxHeight(1 * this.appWidth);
        this.buttonBarBackGround.setPrefHeight(1 * this.appWidth);
        this.buttonBarBackGround.setMinWidth(this.sceneWidth);
        this.buttonBarBackGround.setMaxWidth(this.sceneWidth);
        this.buttonBarBackGround.setPrefWidth(this.sceneWidth);
        this.buttonBarBackGround.setLayoutX(0);
        this.buttonBarBackGround.setLayoutY(this.sceneHeight - 1 * this.appWidth);
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(8.0D);
        this.buttonBarBackGround.setEffect(gaussianBlur);
        // ------------------------------------------------------------

        setCompSize(this.systemFileButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.systemFileButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        setCompSize(this.fileManagerButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.fileManagerButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        setCompSize(this.processButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.processButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        setCompSize(this.occupancyButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.occupancyButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);

        setCompSize(this.helpButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.helpButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        setCompSize(this.minimizeButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.minimizeButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        setCompSize(this.closeButton, 1 * this.appWidth, 1 * this.appWidth);
        setImageViewSize((ImageView) this.closeButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        setCompSize(this.deskButton, 0.8 * this.appWidth, 0.8 * this.appWidth);
        setImageViewSize((ImageView) this.deskButton.getGraphic(), this.appWidth * 0.6, this.appWidth * 0.6);

        this.appBox.setMinHeight(1 * this.appWidth);
        this.appBox.setMaxHeight(1 * this.appWidth);
        this.appBox.setPrefHeight(1 * this.appWidth);
        this.appBox.setPrefWidth(this.appWidth * 10.0);
        this.appBox.setMaxWidth(this.appWidth * 10.0);
        this.appBox.setMinWidth(this.appWidth * 10.0);
        this.appBox.setLayoutX(this.sceneWidth / 2.0 - this.appBox.getWidth() / 2.0);
        this.appBox.setLayoutY(0.0);
        System.out.println(this.appBox.getWidth() + " " + this.appBox.getHeight());

        this.tipBox.setMinHeight(1 * this.appWidth);
        this.tipBox.setMaxHeight(1 * this.appWidth);
        this.tipBox.setPrefHeight(1 * this.appWidth);
        this.tipBox.setMaxWidth(this.timeBox.getWidth() + 1.2 * this.deskButton.getWidth());
        this.tipBox.setMinWidth(this.timeBox.getWidth() + 1.2 * this.deskButton.getWidth());
        this.tipBox.setPrefWidth(this.timeBox.getWidth() + 1.2 * this.deskButton.getWidth());
        this.tipBox.setLayoutX(this.sceneWidth - this.tipBox.getWidth());
        this.tipBox.setLayoutY(0.0);
        this.timeBox.setMinHeight(1.5 * this.appWidth);
        this.tipBox.setMaxHeight(1.5 * this.appWidth);
        System.out.println(this.tipBox.getWidth() + " " + this.tipBox.getHeight());
    }

    // 打开文件管理器
    public void fileAppOpen() throws Exception {
        // 窗口名称
        String stageName = "com/os/apps/fileApp";

        // 检查窗口是否已存在
        Stage stage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing()) removeStage(stageName);

        // 再次检查窗口是否存在
        stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            stage = new Stage();
            new MainUI(stage);
            stageList.add(new StageRecord(stageName, stage));
        }

        // 如果窗口已显示，将其显示在最前面
        if (stage.isShowing()) stage.show();

        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();

        // 更新窗口列表中的信息
        updateStageList(stageName);

        // 设置文件管理器按钮的下划线效果，并修改其样式
        this.fileManagerButton.setUnderline(true);
        this.fileManagerButton.setStyle(buttonStyle);
    }

    // 打开帮助界面
    public void helpAppOpen() {
        // 窗口名称
        String stageName = "com/os/apps/helpApp";

        // 检查窗口是否已存在
        Stage stage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing()) removeStage(stageName);

        // 创建 HelpApp 实例
        HelpApp helpApp = new HelpApp();

        // 再次检查窗口是否存在
        stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();

                // 设置窗口的尺寸限制
                stage.setMinHeight(550.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(800.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setHeight(550.0);
                stage.setWidth(800.0);

                // 使用 HelpApp 实例初始化新窗口
                helpApp.start(stage);

                // 将新窗口记录添加到窗口列表
                stageList.add(new StageRecord(stageName, stage));
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        // 如果窗口已显示，将其显示在最前面
        if (stage.isShowing()) stage.show();

        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();

        // 更新窗口列表中的信息
        updateStageList(stageName);

        // 设置帮助界面按钮的下划线效果，并修改其样式
        this.helpButton.setUnderline(true);
        this.helpButton.setStyle(buttonStyle);
    }

    // 打开设备管理器
    public void occupancyAppOpen() {
        // 窗口名称
        String stageName = "com/os/apps/occupancyApp";

        // 检查窗口是否已存在
        Stage stage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing()) removeStage(stageName);

        // 创建 OccupancyApp 实例
        OccupancyApp occupancyApp = new OccupancyApp();

        // 再次检查窗口是否存在
        stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();

                // 设置窗口的尺寸限制
                stage.setMinHeight(530.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(1000.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setHeight(530.0);
                stage.setWidth(1000.0);

                // 使用 OccupancyApp 实例初始化新窗口
                occupancyApp.start(stage);

                // 将新窗口记录添加到窗口列表
                stageList.add(new StageRecord(stageName, stage));
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        // 如果窗口已显示，将其显示在最前面
        if (stage.isShowing()) stage.show();

        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();

        // 更新窗口列表中的信息
        updateStageList(stageName);

        // 设置设备管理器按钮的下划线效果，并修改其样式
        this.occupancyButton.setUnderline(true);
        this.occupancyButton.setStyle(buttonStyle);
    }

    // 打开进程管理
    public void processAppOpen() {
        // 窗口名称
        String stageName = "com/os/apps/processApp";

        // 检查窗口是否已存在
        Stage stage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing()) removeStage(stageName);

        // 创建 ProcessApp 实例
        ProcessApp processApp = new ProcessApp();

        // 再次检查窗口是否存在
        stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();

                // 设置窗口的尺寸限制
                stage.setMinHeight(500.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(1100.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setHeight(500.0);
                stage.setWidth(1100.0);

                // 使用 ProcessApp 实例初始化新窗口
                processApp.start(stage);

                // 将新窗口记录添加到窗口列表
                stageList.add(new StageRecord(stageName, stage));
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        // 如果窗口已显示，将其显示在最前面
        if (stage.isShowing()) stage.show();

        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();

        // 更新窗口列表中的信息
        updateStageList(stageName);

        // 设置设备管理器按钮的下划线效果，并修改其样式
        this.processButton.setUnderline(true);
        this.processButton.setStyle(buttonStyle);
    }

    // 打开系统文件表
    public void systemFileAppOpen() {
        // 窗口名称
        String stageName = "com/os/apps/systemFileApp";

        // 检查窗口是否已存在
        Stage executableFileStage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (executableFileStage != null && !executableFileStage.isShowing()) removeStage(stageName);

        // 创建 SystemFileApp 实例
        SystemFileApp systemFileApp = new SystemFileApp();

        // 再次检查窗口是否存在
        Stage stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();

                // 设置窗口的尺寸限制
                stage.setMinHeight(500.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(500.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setHeight(500.0);
                stage.setWidth(500.0);

                // 使用 SystemFileApp 实例初始化新窗口
                systemFileApp.start(stage);

                // 将新窗口记录添加到窗口列表
                stageList.add(new StageRecord(stageName, stage));
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        // 如果窗口已显示，将其显示在最前面
        if (stage.isShowing()) stage.show();

        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();

        // 更新窗口列表中的信息
        updateStageList(stageName);

        // 设置设备管理器按钮的下划线效果，并修改其样式
        this.systemFileButton.setUnderline(true);
        this.systemFileButton.setStyle(buttonStyle);
    }

    // 检查窗口是否已存在
    public static Stage checkStage(String name) {
        for (StageRecord stageRecord : stageList) {
            // 如果窗口的名称与传入的名称相匹配，返回该窗口对象
            if (stageRecord.name.equals(name)) {
                return stageRecord.stage;
            }
        }
        return null;
    }

    // 移除指定窗口
    public static void removeStage(String name) {
        for (int i = stageList.size() - 1; i >= 0; --i) {
            // 如果窗口的名称与传入的名称相匹配，移除该窗口记录
            if (stageList.get(i).name.equals(name)) {
                stageList.remove(i);
            }
        }
    }

    // 更新窗口列表中的信息
    public static void updateStageList(String name) {
        for (int i = 0; i < stageList.size(); ++i) {
            // 如果窗口的名称与传入的名称相匹配，更新该窗口的信息
            if (stageList.get(i).name.equals(name)) {
                StageRecord stageRecord = stageList.get(i);
                stageList.remove(stageRecord);
                stageList.add(stageRecord);
                return;
            }
        }
    }

    // 初始化时间显示
    public void timeInit() {
        // 获取当前日期和时间
        Date date = new Date();

        // 格式化日期和时间
        String hour = String.format("%tH", date);
        String minute = String.format("%tM", date);
        String second = String.format("%tS", date);
        String year = String.format("%ty", date);
        String month = String.format("%tm", date);
        String day = String.format("%td", date);

        // 设置时间按钮的最小和最大宽度
        this.timeButton1.setMinWidth(2 * this.appWidth);
        this.timeButton1.setMaxWidth(2 * this.appWidth);
        this.timeButton2.setMinWidth(2 * this.appWidth);
        this.timeButton2.setMaxWidth(2 * this.appWidth);

        // 设置时间按钮的文本格式
        this.timeButton1.setText(hour + ":" + minute + ":" + second);
        this.timeButton2.setText("20" + year + "/" + month + "/" + day);
    }

    // 初始化ui线程
    public void uiThreadInit() {
        UIThread.timeButton1 = this.timeButton1;
        UIThread.timeButton2 = this.timeButton2;
        this.uiThread.init();
        this.uiThread.start();
    }

    // 初始化进程线程
    public void processThreadInit() {
        ProcessManager.init();
        this.processScheduleThread.Init();
        this.processScheduleThread.start();
    }
}
