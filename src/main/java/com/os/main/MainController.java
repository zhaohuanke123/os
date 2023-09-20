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
import com.os.utils.StageRecord;
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

    //region [FXML Comp]
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
    //endregion

    private TreeMap<String, Button> appButtonDict = new TreeMap<>();
    private TreeMap<String, BaseApp> appDict = new TreeMap<>();
    private TreeMap<String, Stage> stageDict = new TreeMap<>();
    public ProcessScheduleThread processScheduleThread = new ProcessScheduleThread();
    public UIThread uiThread = new UIThread();
    Scene mainWindowScene = null;
    Stage primaryStage = null;
    double appWidth;
    public double sceneWidth;
    public double sceneHeight;

    boolean isTop = false;
    boolean haveChanged = true;

    public void init(Scene scene, Stage stage) throws URISyntaxException {
        _instance = this;

        appButtonDict.put("com/os/apps/systemFileApp", systemFileButton);
        appButtonDict.put("com/os/apps/processApp", processButton);
        appButtonDict.put("com/os/apps/occupancyApp", occupancyButton);
        appButtonDict.put("com/os/apps/helpApp", helpButton);
        appButtonDict.put("com/os/apps/fileApp", fileManagerButton);

        appDict.put("com/os/apps/systemFileApp", new SystemFileApp());
        appDict.put("com/os/apps/processApp", new ProcessApp());
        appDict.put("com/os/apps/occupancyApp", new OccupancyApp());
        appDict.put("com/os/apps/helpApp", new HelpApp());
        appDict.put("com/os/apps/fileApp", new MainUI());

        this.mainWindowScene = scene;
        this.primaryStage = stage;
        this.primaryStage.setOnCloseRequest(event -> {
            System.out.println("结束");
            System.exit(0);
        });

        this.iconInit();
        this.timeInit();
        OccupancyManager.init();
        MainUI.loadData();
        this.processThreadInit();
        this.uiThreadInit();
    }

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

        minimizeOnShowApp(true);

        this.primaryStage.setIconified(true);
    }

    public void adaptWindow() {
        //
        this.MainWindow.setPrefSize(this.mainWindowScene.getWidth(), this.mainWindowScene.getHeight());

        // 初始化任务栏
        CompSet.setCompFixSize(this.buttonBar, this.sceneWidth, 1 * this.appWidth);
        this.buttonBar.setLayoutX(0);
        this.buttonBar.setLayoutY(this.sceneHeight - 1 * this.appWidth);

        //
        CompSet.setCompFixSize(this.systemFileButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.systemFileButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompFixSize(this.fileManagerButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.fileManagerButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompFixSize(this.processButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.processButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompFixSize(this.occupancyButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.occupancyButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);

        //
        CompSet.setCompFixSize(this.helpButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.helpButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompFixSize(this.minimizeButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.minimizeButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompFixSize(this.closeButton, 1 * this.appWidth, 1 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.closeButton.getGraphic(), this.appWidth * 0.7, this.appWidth * 0.7);
        CompSet.setCompFixSize(this.deskButton, 0.2 * this.appWidth, 0.8 * this.appWidth);
        CompSet.setImageViewFixSize((ImageView) this.deskButton.getGraphic(), this.appWidth * 0.6, this.appWidth * 0.6);

        //
        CompSet.setCompFixSize(this.appBox, this.appWidth * 10.0, 1 * this.appWidth);
        this.appBox.setLayoutX(this.sceneWidth / 2.0 - this.appBox.getWidth() / 2.0);
        this.appBox.setLayoutY(0.0);

        //
        CompSet.setCompFixSize(this.tipBox, this.timeBox.getWidth() + 1 * this.deskButton.getWidth(), 1 * this.appWidth);
        this.tipBox.setLayoutX(this.sceneWidth - 1 * this.tipBox.getWidth());
        this.tipBox.setLayoutY(0.0);
    }

    // 返回桌面
    private void toDesk() {
        if (this.haveChanged) {
            this.haveChanged = false;

            //  this.primaryStage.toFront();
            minimizeOnShowApp(!this.isTop);

            this.isTop = !this.isTop;
            this.haveChanged = !this.haveChanged;
        }

    }

    private void minimizeOnShowApp(Boolean isMinimize) {
        stageDict.forEach((stageName, stage) -> {
            if (stageName.contains("apps")) {
                if (stage != null) {
                    stage.setIconified(isMinimize);
                }
            }
        });

        if (MainUI.fileAppAdditionStageList != null) {
            for (int i = 0; i < MainUI.fileAppAdditionStageList.size(); ++i) {
                Stage stage1 = MainUI.fileAppAdditionStageList.get(i);

                if (stage1 != null && !stage1.isShowing()) {
                    MainUI.fileAppAdditionStageList.remove(stage1);
                } else {
                    if (stage1 != null) {
                        stage1.setIconified(isMinimize);
                    }
                }
            }
        }

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
                stageDict.put(stageName, stage);
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
//        updateStageList(stageName);

        Button button = appButtonDict.get(stageName);
        if (button != null) {
            // 设置设备管理器按钮的下划线效果，并修改其样式
            button.setUnderline(true);
            String buttonStyle = "-fx-background-color: transparent,aliceblue;" +
                    "-fx-background-radius: 12;" +
                    "-fx-text-fill: black;" +
                    "-fx-effect: dropshadow( three-pass-box, rgba(0, 0, 0, 0.6), 3, 0, 0, 1);";
            button.setStyle(buttonStyle);
        }
    }

    // 检查窗口是否已存在
    public Stage checkStage(String name) {
        return stageDict.get(name);
    }

    // 移除指定窗口
    public void removeStage(String name) {
        stageDict.remove(name);
    }

    // 更新窗口列表中的信息
    public void updateStageList(String name) {
        stageDict.put(name, checkStage(name));
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

        appButtonDict.forEach((stageName, button) -> {
            button.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    this.OnAppOpen(stageName, appDict.get(stageName));
                }
            });
        });

        this.deskButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                this.toDesk();
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
        CompSet.setCompFixSize(this.timeButton2, 2 * this.appWidth, -1);
        CompSet.setCompFixSize(this.timeBox, 2 * this.appWidth, -1);
    }

    // 初始化ui线程
    private void uiThreadInit() {
        this.uiThread.init();
        this.uiThread.start();
    }

    // 初始化进程线程
    private void processThreadInit() {
        ProcessManager.init();
        this.processScheduleThread.Init();
        this.processScheduleThread.start();
    }

    public void Update() {
        appButtonUpdate();
        timeUpdate();
    }

    private void appButtonUpdate() {
        appButtonDict.forEach((stageName, button) -> {
            Stage stage = checkStage(stageName);
            if (stage != null && !stage.isShowing()) {
                button.setStyle("");
            }
        });
    }

    private void timeUpdate() {
        if (timeButton1 != null && timeButton2 != null) {
            Platform.runLater(() -> {
                Date date = new Date();
                timeButton1.setText(
                        String.format("%tH", date) + ":" +
                                String.format("%tM", date) + ":" +
                                String.format("%tS", date));
                timeButton2.setText("20" + String.format("%ty", date) + "/" +
                        String.format("%tm", date) + "/" +
                        String.format("%td", date));
            });
        }
    }
}
