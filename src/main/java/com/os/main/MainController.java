package com.os.main;

import com.os.apps.BaseApp;
import com.os.apps.fileApp.app.MainUI;
import com.os.apps.helpApp.HelpApp;
import com.os.apps.occupancyApp.OccupancyApp;
import com.os.apps.processApp.ProcessApp;
import com.os.apps.systemFileApp.SystemFileApp;
import com.os.utils.fileSystem.FAT;
import com.os.utils.process.OccupancyManager;
import com.os.utils.process.ProcessManager;
import com.os.utils.process.ProcessScheduleThread;
import com.os.utils.ui.CompSet;
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
import java.util.*;

public class MainController {
    private static MainController _instance;

    public static MainController getInstance() {
        return _instance;
    }

    public static final String packageName = "com/os/apps/";
    // 字典
    private TreeMap<String, Button> appButtonDict;
    public ProcessScheduleThread processScheduleThread = new ProcessScheduleThread();
    public UIThread uiThread = new UIThread();
    public Pane buttonBarBackGround;
    Scene mainWindowScene = null;
    Stage primaryStage = null;
    public static Vector<StageRecord> stageList = new Vector<>();
    double appWidth;
    public double sceneWidth;
    public double sceneHeight;
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
    private Button fileManagerButton;
    @FXML
    private Button helpButton;
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
    boolean isTop = false;
    boolean haveChanged = true;
    private final String buttonStyle =
            "-fx-background-color: transparent,aliceblue;" +
                    "-fx-background-radius: 12;" +
                    "-fx-text-fill: black;" +
                    "-fx-effect: dropshadow( three-pass-box, rgba(0, 0, 0, 0.6), 3, 0, 0, 1);";

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
            if (stageRecord.name.contains("App")) {
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

    public void adaptWindow() {
        //
        this.MainWindow.setPrefSize(this.mainWindowScene.getWidth(), this.mainWindowScene.getHeight());

        // 初始化任务栏
        CompSet.setCompSize(this.buttonBar, this.sceneWidth, 1 * this.appWidth);
        this.buttonBar.setLayoutX(0);
        this.buttonBar.setLayoutY(this.sceneHeight - 1 * this.appWidth);

        // 初始化任务栏背景
        CompSet.setCompSize(this.buttonBarBackGround, this.sceneWidth, 1 * this.appWidth);
        this.buttonBarBackGround.setLayoutX(0);
        this.buttonBarBackGround.setLayoutY(this.sceneHeight - 1 * this.appWidth);
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(8.0D);
        this.buttonBarBackGround.setEffect(gaussianBlur);

        //
        CompSet.setCompSize(this.systemFileButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.systemFileButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompSize(this.fileManagerButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.fileManagerButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompSize(this.processButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.processButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompSize(this.occupancyButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.occupancyButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);

        //
        CompSet.setCompSize(this.helpButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.helpButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompSize(this.minimizeButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.minimizeButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompSize(this.closeButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.closeButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompSize(this.deskButton, 0.2 * this.appWidth, 0.8 * this.appWidth);
        CompSet.setImageViewSize((ImageView) this.deskButton.getGraphic(), this.appWidth * 0.6, this.appWidth * 0.6);

        //
        CompSet.setCompSize(this.appBox, this.appWidth * 10.0, 1 * this.appWidth);
        this.appBox.setLayoutX(this.sceneWidth / 2.0 - this.appBox.getWidth() / 2.0);
        this.appBox.setLayoutY(0.0);

        //
        CompSet.setCompSize(this.tipBox, this.timeBox.getWidth() + 1 * this.deskButton.getWidth(), 1 * this.appWidth);
        this.tipBox.setLayoutX(this.sceneWidth - 1 * this.tipBox.getWidth());
        this.tipBox.setLayoutY(0.0);
    }

    // 返回桌面
    private void toDesk() {
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

    // 打开文件管理器
    private void fileAppOpen() throws Exception {
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

    private void OnAppOpen(String stageName, BaseApp app) {
        // 检查窗口是否已存在
        Stage stage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing()) removeStage(stageName);
        // 再次检查窗口是否存在
        stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();
                // 使用 SystemFileApp 实例初始化新窗口
                app.start(stage);
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

        Button button = appButtonDict.get(stageName);
        if (button != null) {
            // 设置设备管理器按钮的下划线效果，并修改其样式
            button.setUnderline(true);
            button.setStyle(buttonStyle);
        }
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

    public void init(Scene scene, Stage stage) throws URISyntaxException {
        _instance = this;

        appButtonDict = new TreeMap<>();
        appButtonDict.put("systemFileApp", systemFileButton);
        appButtonDict.put("processApp", processButton);
        appButtonDict.put("occupancyApp", occupancyButton);
        appButtonDict.put("com/os/apps/fileApp", fileManagerButton);
        appButtonDict.put("helpApp", helpButton);

        this.mainWindowScene = scene;
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

    private void iconInit() {
        this.appWidth = this.mainWindowScene.getHeight() / 15.0;
        this.sceneWidth = this.mainWindowScene.getWidth();
        this.sceneHeight = this.mainWindowScene.getHeight();

        this.background.setLayoutX(0.0);
        this.background.setLayoutY(0.0);
        this.background.fitWidthProperty().bind(this.mainWindowScene.widthProperty());
        this.background.fitHeightProperty().bind(this.mainWindowScene.heightProperty());
        this.background.setPreserveRatio(false);
        this.background.setVisible(true);

        this.systemFileButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
//                System.out.println("executableFileApp open success");
                this.OnAppOpen("systemFileApp", new SystemFileApp());
            }

        });
        this.processButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
//                System.out.println("processApp open success");
                this.OnAppOpen("processApp", new ProcessApp());
            }

        });
        this.occupancyButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
//                System.out.println("occupancyApp open success");
                this.OnAppOpen("occupancyApp", new OccupancyApp());
            }

        });
        this.helpButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
//                System.out.println("helpApp open success");
                this.OnAppOpen("helpApp", new HelpApp());
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

        //region [设置按钮的提示信息]
        Button[] buttons = new Button[]{
                this.systemFileButton, this.fileManagerButton,
                this.processButton, this.occupancyButton,
                this.helpButton, this.minimizeButton,
                this.closeButton, this.deskButton};
        String[] buttonNames = new String[]{
                "系统文件表", "文件管理器",
                "进程管理器", "占用管理器",
                "帮助", "最小化",
                "关闭", "显示桌面"};
        String tooltipStyle = "-fx-font-size: 12";
        for (int i = 0; i < buttons.length; ++i) {
            Tooltip tooltip = new Tooltip(buttonNames[i]);
            tooltip.setStyle(tooltipStyle);
            buttons[i].setTooltip(tooltip);
        }
        //endregion

        UIThread.mainButtons = buttons;
        UIThread.stageList = stageList;
    }

    // 初始化时间显示
    private void timeInit() {
        // 获取当前日期和时间
        Date date = new Date();
        // 设置时间按钮的文本格式
        this.timeButton1.setText(
                String.format("%tH", date) + ":" +
                        String.format("%tM", date) + ":" +
                        String.format("%tS", date));
        this.timeButton2.setText("20" + String.format("%ty", date) + "/" +
                String.format("%tm", date) + "/" +
                String.format("%td", date));

        // 设置时间按钮的最小和最大宽度
        CompSet.setCompSize(this.timeButton2, 2 * this.appWidth, -1);
        CompSet.setCompSize(this.timeBox, 2 * this.appWidth, -1);
    }

    // 初始化ui线程
    private void uiThreadInit() {
        UIThread.timeButton1 = this.timeButton1;
        UIThread.timeButton2 = this.timeButton2;
        this.uiThread.init();
        this.uiThread.start();
    }

    // 初始化进程线程
    private void processThreadInit() {
        ProcessManager.init();
        this.processScheduleThread.Init();
        this.processScheduleThread.start();
    }

    public void appButtonUpdate() {
        appButtonDict.forEach((stageName, button) -> {
            Stage stage = checkStage(stageName);
            if (stage != null && !stage.isShowing()) {
                button.setStyle("");
            }
        });
    }
}
