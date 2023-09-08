package com.os.main;

import com.os.apps.fileApp.app.MainUI;
import com.os.utils.fileSystem.FAT;
import com.os.apps.helpApp.HelpApp;
import com.os.apps.occupancyApp.OccupancyApp;
import com.os.apps.processApp.ProcessApp;
import com.os.apps.systemFileApp.SystemFileApp;
import com.os.utils.process.OccupancyManager;
import com.os.utils.process.ProcessManager;
import com.os.utils.process.ProcessScheduleThread;
import com.os.utils.ui.StageRecord;
import com.os.utils.ui.UIThread;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class MainController {
    final Label menu = new Label();
    public ProcessScheduleThread processScheduleThread = new ProcessScheduleThread();
    public UIThread uiThread = new UIThread();
    public Pane buttonBarBackGround;
    ArrayList<Button> buttonList = new ArrayList<>();
    Scene scene = null;
    Stage primaryStage = null;
    Button temporaryButton = null;
    public static Vector<StageRecord> stageList = new Vector<>();
    double appWidth;
    double distance;
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
    private Button executableFileButton;
    @FXML
    private Button processButton;
    @FXML
    private Button occupancyButton;
    @FXML
    private HBox tipBox;
    @FXML
    private Separator separator1;
    @FXML
    private Button minimizeButton;
    @FXML
    private Separator separator2;
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

    @FXML
    void updateTimeButton1(ActionEvent event) {
    }

    @FXML
    void updateTimeButton2(ActionEvent event) {
    }

    public void iconInit() {
        this.appWidth = this.scene.getHeight() / 15.0;
        this.sceneWidth = this.scene.getWidth();
        this.sceneHeight = this.scene.getHeight();
        this.background.setLayoutX(0.0);
        this.background.setLayoutY(0.0);
        this.background.fitWidthProperty().bind(this.scene.widthProperty());
        this.background.fitHeightProperty().bind(this.scene.heightProperty());
        this.background.setPreserveRatio(false);
        this.background.setVisible(true);

        this.executableFileButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                System.out.println("executableFileApp open success");
                MainController.this.executableFileAppOpen();
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

        Button[] buttons = new Button[]{this.executableFileButton, this.fileManagerButton,
                this.processButton, this.occupancyButton, this.helpButton};
        Tooltip tooltip = new Tooltip("系统文件表");
        tooltip.setStyle("-fx-font-size: 12");
        this.executableFileButton.setTooltip(tooltip);
        tooltip = new Tooltip("文件管理器");
        tooltip.setStyle("-fx-font-size: 12");
        this.fileManagerButton.setTooltip(tooltip);
        tooltip = new Tooltip("进程管理器");
        tooltip.setStyle("-fx-font-size: 12");
        this.processButton.setTooltip(tooltip);
        tooltip = new Tooltip("占用管理器");
        tooltip.setStyle("-fx-font-size: 12");
        this.occupancyButton.setTooltip(tooltip);
        tooltip = new Tooltip("帮助");
        tooltip.setStyle("-fx-font-size: 12");
        this.helpButton.setTooltip(tooltip);
        tooltip = new Tooltip("最小化");
        tooltip.setStyle("-fx-font-size:12");
        this.minimizeButton.setTooltip(tooltip);
        tooltip = new Tooltip("关闭");
        tooltip.setStyle("-fx-font-size: 12");
        this.closeButton.setTooltip(tooltip);
        tooltip = new Tooltip("显示桌面");
        tooltip.setStyle("-fx-font-size: 12");
        this.deskButton.setTooltip(tooltip);

        UIThread.mainButtons = buttons;
        UIThread.stageList = stageList;
    }

    public void toDesk() {
        if (this.haveChanged) {
            this.haveChanged = false;
            if (!this.isTop) {
                this.primaryStage.toFront();

                int i;
                Stage stage1;
                for (i = 0; i < stageList.size(); ++i) {
                    stage1 = stageList.get(i).stage;
                    if (stage1 != null && stage1.isShowing()) {
                        stage1.setIconified(true);
                    }
                }

                if (MainUI.fileAppAdditionStageList != null) {
                    for (i = 0; i < MainUI.fileAppAdditionStageList.size(); ++i) {
                        stage1 = MainUI.fileAppAdditionStageList.get(i);
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
                int i;
                Stage stage1;
                for (i = 0; i < stageList.size(); ++i) {
                    stage1 = stageList.get(i).stage;
                    if (stage1 != null && stage1.isShowing()) {
                        stage1.setIconified(false);
                    }
                }

                if (MainUI.fileAppAdditionStageList != null) {
                    for (i = 0; i < MainUI.fileAppAdditionStageList.size(); ++i) {
                        stage1 = MainUI.fileAppAdditionStageList.get(i);
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

    private void setButtonSize(Button button, double width, double height) {
        button.setPrefSize(width, height);
        button.setMinSize(width, height);
        button.setMaxSize(width, height);
        button.resize(width, height);
    }

    private void setButtonImageViewSize(ImageView button, double width, double height) {
        button.setFitWidth(width);
        button.setFitHeight(height);
    }

    public void adaptWindow() {
        this.MainWindow.setPrefSize(this.scene.getWidth(), this.scene.getHeight());
        System.out.println("MainWindow:" + this.MainWindow.getWidth() + "," + this.MainWindow.getHeight());
        // 初始化任务栏
        this.buttonBar.setMaxHeight(1.5 * this.appWidth);
        this.buttonBar.setMinHeight(1.5 * this.appWidth);
        this.buttonBar.setPrefHeight(1.5 * this.appWidth);
        this.buttonBar.setMaxWidth(this.sceneWidth);
        this.buttonBar.setMinWidth(this.sceneWidth);
        this.buttonBar.setPrefWidth(this.sceneWidth);
        this.buttonBar.setLayoutX(0);
        this.buttonBar.setLayoutY(this.sceneHeight - 1.4 * this.appWidth);

        // 初始化任务栏背景
        this.buttonBarBackGround.setMinHeight(1.5 * this.appWidth);
        this.buttonBarBackGround.setMaxHeight(1.5 * this.appWidth);
        this.buttonBarBackGround.setPrefHeight(1.5 * this.appWidth);
        this.buttonBarBackGround.setMinWidth(this.sceneWidth);
        this.buttonBarBackGround.setMaxWidth(this.sceneWidth);
        this.buttonBarBackGround.setPrefWidth(this.sceneWidth);
        this.buttonBarBackGround.setLayoutX(0);
        this.buttonBarBackGround.setLayoutY(this.sceneHeight - 1.4 * this.appWidth);
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(16.0D);
        this.buttonBarBackGround.setEffect(gaussianBlur);
        // ------------------------------------------------------------

        setButtonSize(this.executableFileButton, 1.2 * this.appWidth, 1.2 * this.appWidth);
        setButtonImageViewSize((ImageView) this.executableFileButton.getGraphic(), this.appWidth * 0.9, this.appWidth * 0.9);
        setButtonSize(this.fileManagerButton, 1.2 * this.appWidth, 1.2 * this.appWidth);
        setButtonImageViewSize((ImageView) this.processButton.getGraphic(), this.appWidth * 0.9, this.appWidth * 0.9);
        setButtonSize(this.processButton, 1.2 * this.appWidth, 1.2 * this.appWidth);
        setButtonImageViewSize((ImageView) this.processButton.getGraphic(), this.appWidth * 0.9, this.appWidth * 0.9);
        setButtonSize(this.occupancyButton, 1.2 * this.appWidth, 1.2 * this.appWidth);
        setButtonImageViewSize((ImageView) this.occupancyButton.getGraphic(), this.appWidth * 0.9, this.appWidth * 0.9);

        setButtonSize(this.helpButton, 0.8 * this.appWidth, 0.8 * this.appWidth);
        setButtonImageViewSize((ImageView) this.helpButton.getGraphic(), this.appWidth * 0.8, this.appWidth * 0.8);
        setButtonSize(this.minimizeButton, 0.8 * this.appWidth, 0.8 * this.appWidth);
        setButtonImageViewSize((ImageView) this.minimizeButton.getGraphic(), this.appWidth * 0.8, this.appWidth * 0.8);
        setButtonSize(this.closeButton, 0.8 * this.appWidth, 0.8 * this.appWidth);
        setButtonImageViewSize((ImageView) this.closeButton.getGraphic(), this.appWidth * 0.8, this.appWidth * 0.8);
        setButtonSize(this.deskButton, 0.8 * this.appWidth, 0.8 * this.appWidth);
        setButtonImageViewSize((ImageView) this.deskButton.getGraphic(), this.appWidth * 0.8, this.appWidth * 0.8);

        this.appBox.setMinHeight(1.5 * this.appWidth);
        this.appBox.setMaxHeight(1.5 * this.appWidth);
        this.appBox.setPrefHeight(1.5 * this.appWidth);
        this.appBox.setPrefWidth(this.appWidth * 10.0);
        this.appBox.setMaxWidth(this.appWidth * 10.0);
        this.appBox.setMinWidth(this.appWidth * 10.0);
        this.appBox.setLayoutX(this.sceneWidth / 2.0 - this.appBox.getWidth() / 2.0);
        this.appBox.setLayoutY(0.0);
        System.out.println(this.appBox.getWidth() + " " + this.appBox.getHeight());

        this.tipBox.setMinHeight(1.5 * this.appWidth);
        this.tipBox.setMaxHeight(1.5 * this.appWidth);
        this.tipBox.setPrefHeight(1.5 * this.appWidth);
        this.tipBox.setMaxWidth(this.closeButton.getWidth() * 6.0);
        this.tipBox.setMinWidth(this.closeButton.getWidth() * 6.0);
        this.tipBox.setPrefWidth(this.closeButton.getWidth() * 6.0);
        this.tipBox.setLayoutX(this.sceneWidth - this.tipBox.getWidth());
        this.tipBox.setLayoutY(0.0);
        this.timeBox.setMinHeight(1.5 * this.appWidth);
        this.tipBox.setMaxHeight(1.5 * this.appWidth);
        System.out.println(this.tipBox.getWidth() + " " + this.tipBox.getHeight());
    }

    public static void removeStage(String name) {
        for (int i = stageList.size() - 1; i >= 0; --i) {
            if (stageList.get(i).name.equals(name)) {
                stageList.remove(i);
            }
        }
    }

    public void fileAppOpen() throws Exception {
        Stage stage = checkStage("com/os/apps/fileApp");
        if (stage != null && !stage.isShowing()) {
            removeStage("com/os/apps/fileApp");
        }

        stage = checkStage("com/os/apps/fileApp");
        if (stage == null) {
            stage = new Stage();
            new MainUI(stage);
            stageList.add(new StageRecord("com/os/apps/fileApp", stage));
        }

        if (stage.isShowing()) {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("com/os/apps/fileApp");
        this.fileManagerButton.setUnderline(true);
        this.fileManagerButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n    -fx-background-radius: 12;\n    -fx-text-fill: black;\n    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }

    public void helpAppOpen() {
        Stage stage = checkStage("com/os/apps/helpApp");
        if (stage != null && !stage.isShowing()) {
            removeStage("com/os/apps/helpApp");
        }

        HelpApp helpApp = new HelpApp();
        stage = checkStage("com/os/apps/helpApp");
        if (stage == null) {
            try {
                stage = new Stage();
//                double ratioW = this.primaryStage.getWidth() / 1920.0;
//                double ratioH = this.primaryStage.getHeight() / 1080.0;
                stage.setMinHeight(550.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(800.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setWidth(800.0);
                stage.setHeight(550.0);
                helpApp.start(stage);
                stageList.add(new StageRecord("com/os/apps/helpApp", stage));
            } catch (IOException var7) {
                System.out.println(Arrays.toString(var7.getStackTrace()));
            }
        }

        if (stage.isShowing()) {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("com/os/apps/helpApp");
        this.helpButton.setUnderline(true);
        this.helpButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n    -fx-background-radius:20;\n    -fx-text-fill: black;\n    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }

    public void occupancyAppOpen() {
        Stage stage = checkStage("com/os/apps/occupancyApp");
        if (stage != null && !stage.isShowing()) {
            removeStage("com/os/apps/occupancyApp");
        }

        OccupancyApp occupancyApp = new OccupancyApp();
        stage = checkStage("com/os/apps/occupancyApp");
        if (stage == null) {
            try {
                stage = new Stage();
//                double ratioW = this.primaryStage.getWidth() / 1920.0;
//                double ratioH = this.primaryStage.getHeight() / 1080.0;
                stage.setMinHeight(530.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(1000.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setWidth(1000.0);
                stage.setHeight(530.0);
                occupancyApp.start(stage);
                stageList.add(new StageRecord("com/os/apps/occupancyApp", stage));
            } catch (IOException var7) {
                System.out.println(Arrays.toString(var7.getStackTrace()));
            }
        }

        if (stage.isShowing()) {
            stage.show();
        }

        stage.setOnCloseRequest(event -> {
            MainController.this.occupancyButton.setUnderline(false);
            MainController.this.occupancyButton.setStyle("");
        });
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("com/os/apps/occupancyApp");
        this.occupancyButton.setUnderline(true);
        this.occupancyButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n    -fx-background-radius: 12;\n    -fx-text-fill: black;\n    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }

    public void processAppOpen() {
        Stage stage = checkStage("com/os/apps/processApp");
        if (stage != null && !stage.isShowing()) {
            removeStage("com/os/apps/processApp");
        }

        ProcessApp processApp = new ProcessApp();
        stage = checkStage("com/os/apps/processApp");
        if (stage == null) {
            try {
                stage = new Stage();
//                double ratioW = this.primaryStage.getWidth() / 1920.0;
//                double ratioH = this.primaryStage.getHeight() / 1080.0;
                stage.setMinHeight(500.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(1100.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setWidth(1100.0);
                stage.setHeight(500.0);
                processApp.start(stage);
                stageList.add(new StageRecord("com/os/apps/processApp", stage));
            } catch (IOException var7) {
                System.out.println(Arrays.toString(var7.getStackTrace()));
            }
        }

        if (stage.isShowing()) {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("com/os/apps/processApp");
        this.processButton.setUnderline(true);
        this.processButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n    -fx-background-radius: 12;\n    -fx-text-fill: black;\n    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }

    public void executableFileAppOpen() {
        Stage executableFileStage = checkStage("com/os/apps/systemFileApp");
        if (executableFileStage != null && !executableFileStage.isShowing()) {
            removeStage("com/os/apps/systemFileApp");
        }

        SystemFileApp systemFileApp = new SystemFileApp();
        Stage stage = checkStage("com/os/apps/systemFileApp");
        if (stage == null) {
            try {
                stage = new Stage();
//                double ratioW = this.primaryStage.getWidth() / 1920.0;
//                double ratioH = this.primaryStage.getHeight() / 1080.0;
                stage.setMinHeight(500.0);
                stage.setMaxHeight(this.primaryStage.getHeight() - 1.4 * this.appWidth);
                stage.setMinWidth(500.0);
                stage.setMaxWidth(this.primaryStage.getWidth());
                stage.setWidth(500.0);
                stage.setHeight(500.0);
                systemFileApp.start(stage);
                stageList.add(new StageRecord("com/os/apps/systemFileApp", stage));
            } catch (IOException var8) {
                System.out.println(Arrays.toString(var8.getStackTrace()));
            }
        }

        if (stage.isShowing()) {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("com/os/apps/systemFileApp");
        this.executableFileButton.setUnderline(true);
        this.executableFileButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n    -fx-background-radius: 12;\n    -fx-text-fill: black;\n    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }

    public static Stage checkStage(String name) {
        for (StageRecord stageRecord : stageList) {
            if (stageRecord.name.equals(name)) {
                return stageRecord.stage;
            }
        }

        return null;
    }

    public static void updateStageList(String name) {
        for (int i = 0; i < stageList.size(); ++i) {
            if (stageList.get(i).name.equals(name)) {
                StageRecord stageRecord = stageList.get(i);
                stageList.remove(stageRecord);
                stageList.add(stageRecord);
                return;
            }
        }

    }

    public void timeInit() {
        Date date = new Date();
        String hour = String.format("%tH", date);
        String minute = String.format("%tM", date);
        String second = String.format("%tS", date);
        String year = String.format("%ty", date);
        String month = String.format("%tm", date);
        String day = String.format("%td", date);
        this.timeButton1.setMinWidth(2 * this.appWidth);
        this.timeButton1.setMaxWidth(2 * this.appWidth);
        this.timeButton2.setMinWidth(2 * this.appWidth);
        this.timeButton2.setMaxWidth(2 * this.appWidth);
        this.timeButton1.setText(hour + ":" + minute + ":" + second);
        this.timeButton2.setText("20" + year + "/" + month + "/" + day);
    }

    public void uiThreadInit() {
        UIThread.timeButton1 = this.timeButton1;
        UIThread.timeButton2 = this.timeButton2;
        this.uiThread.init();
        this.uiThread.start();
    }

    public void processThreadInit() {
        ProcessManager.init();
        this.processScheduleThread.Init();
        this.processScheduleThread.start();
    }
}
