package com.os.main;

import com.os.apps.fileApp.FileApp;
import com.os.utils.fileSystem.FAT;
import com.os.utils.processSystem.OccupancyManager;
import com.os.utils.processSystem.ProcessManager;
import com.os.utils.processSystem.ProcessScheduleThread;
import com.os.utils.scene.SceneManager;
import com.os.utils.uiUtil.CompSet;
import com.os.utils.uiUtil.UIThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MainController {
    private static MainController _instance;

    public static MainController getInstance() {
        if (_instance == null) {
            throw new NullPointerException("MainController is null");
        }
        return _instance;
    }

    @FXML
    private Pane MainWindow;
    @FXML
    private ImageView background;

    @FXML
    private Pane buttonBar;
    @FXML
    private HBox appBox;
    @FXML
    private Button systemFileButton;  // 系统文件按钮
    @FXML
    private Button processButton;  // 进程管理按钮
    @FXML
    private Button occupancyButton;  // 占用管理按钮
    @FXML
    private Button fileManagerButton;  // 文件管理按钮
    @FXML
    private Button helpButton;  // 帮助按钮
    @FXML
    private HBox tipBox;  // 包含时间和返回桌面按钮的HBox
    @FXML
    private Button minimizeButton;  // 最小化窗口按钮
    @FXML
    private Button closeButton;  // 关闭窗口按钮
    private final TreeMap<String, Button> appButtonDict = new TreeMap<>();


    @FXML
    private VBox timeBox;  // 包含2种时间显示的VBox
    @FXML
    private Button timeButton1;  // 时间显示
    @FXML
    private Button timeButton2;  // 日期显示
    @FXML
    private Button deskButton;  // 返回桌面按钮
    private final TimeModel timeModel = new TimeModel();

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

        appButtonDict.put("systemFileApp", systemFileButton);
        appButtonDict.put("processApp", processButton);
        appButtonDict.put("occupancyApp", occupancyButton);
        appButtonDict.put("helpApp", helpButton);
        appButtonDict.put("fileApp", fileManagerButton);

        this.mainWindowScene = scene;
        this.primaryStage = stage;
        this.primaryStage.setOnCloseRequest(event -> System.exit(0));

        this.initBackGround();
        this.iconInit();
        this.timeInit();
        OccupancyManager.init();
        FileApp.loadData();
        this.processThreadInit();
        this.uiThreadInit();
    }

    private void initBackGround() {
        this.appWidth = this.mainWindowScene.getHeight() / 15.0;
        this.sceneWidth = this.mainWindowScene.getWidth();
        this.sceneHeight = this.mainWindowScene.getHeight();

        this.background.setLayoutX(0.0);
        this.background.setLayoutY(0.0);
        this.background.fitWidthProperty().bind(this.mainWindowScene.widthProperty());
        this.background.fitHeightProperty().bind(this.mainWindowScene.heightProperty());
        this.background.setPreserveRatio(false);
        this.background.setVisible(true);
    }

    @FXML
    void closeWindow(MouseEvent event) {
        FAT.closeAll();
        if (FileApp.fat != null) {
            FileApp.saveData();
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
        CompSet.setCompFixSize(this.tipBox, this.timeBox.getWidth() + this.deskButton.getWidth(), 1 * this.appWidth);
        this.tipBox.setLayoutX(this.sceneWidth - this.tipBox.getWidth());
        this.tipBox.setLayoutY(0.0);
    }

    private void iconInit() {
        appButtonDict.forEach((stageName, button) -> button.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                SceneManager.getInstance().AppStart(stageName);

                // 修改按钮样式
                button.setUnderline(true);
                button.setId("appButtonSelected");
            }
        }));

        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    appButtonDict.forEach((stageName, button) -> {
                        Stage stage = SceneManager.getInstance().checkStage(stageName);
                        if (stage != null && !stage.isShowing()) {
                            button.setId("appButton");
                        }
                    });
                });

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ignore) {
                }
            }
        }).start();

        this.deskButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                this.toDesk();
            }
        });
    }

    // 初始化时间显示
    private void timeInit() {
        timeButton1.textProperty().bind(timeModel.time1Property());
        timeButton2.textProperty().bind(timeModel.time2Property());
        SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_hms = new SimpleDateFormat("HH:mm:ss");

        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    Date date = new Date();
                    timeModel.setTime1(sdf_hms.format(date));
                    timeModel.setTime2(sdf_ymd.format(date));
                });

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException ignore) {
                }
            }
        }).start();

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

    // 返回桌面
    private void toDesk() {
        if (this.haveChanged) {
            this.haveChanged = false;

            minimizeOnShowApp(!this.isTop);

            this.isTop = !this.isTop;
            this.haveChanged = !this.haveChanged;
        }

    }

    private void minimizeOnShowApp(Boolean isMinimize) {
        SceneManager.getInstance().setAllStageState(isMinimize);

        FileApp.minimizeOnShowApp(isMinimize);
    }

    private void OnAppOpen(String stageName) {
        // 检查窗口是否已存在
        Stage stage = SceneManager.getInstance().checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing())
            SceneManager.getInstance().removeStage(stageName);

        // 再次检查窗口是否存在
        stage = SceneManager.getInstance().checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();
                // 使用 SystemFileApp 实例初始化新窗口
                SceneManager.getInstance().getApp(stageName).start(stage);
                // 将新窗口记录添加到窗口列表
                SceneManager.getInstance().addStage(stageName, stage);
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
        SceneManager.getInstance().updateStageList(stageName);


    }
}
