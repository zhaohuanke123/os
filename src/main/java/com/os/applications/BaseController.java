package com.os.applications;

import com.os.applications.fileApp.application.HelpDialogApplication;
import com.os.utility.uiUtil.DrawUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    protected boolean haveResize = true;  // 是否允许窗口调整大小
    protected boolean isMax = false;  // 窗口是否已最大化
    protected Stage stage;  // 窗口对象
    protected double xOffset = 0.0;  // 鼠标拖动时的X偏移
    protected double yOffset = 0.0;  // 鼠标拖动时的Y偏移
    protected double preX = 0.0;  // 记录最大化前的X坐标
    protected double preY = 0.0;  // 记录最大化前的Y坐标
    protected double preWidth = 0.0;  // 记录最大化前的宽度
    protected double preHeight = 0.0;  // 记录最大化前的高度

    @FXML
    protected BorderPane titleBar;  // 标题栏界面组件
    @FXML
    protected HBox titleBarL;  // 标题栏左侧部分
    @FXML
    protected HBox titleBarR;  // 标题栏右侧部分
    @FXML
    public Label title;  // 标题标签
    @FXML
    protected AnchorPane topMainPane;  // 顶部主面板
    protected DrawUtil drawUtil;

    // 初始化方法，用于设置窗口对象
    public void init(Stage stage) {
        this.stage = stage;

        // 创建绘图工具
        drawUtil = new DrawUtil();
        drawUtil.addDrawFunc(stage, this.topMainPane);

        // 监听窗口大小变化，根据窗口大小自适应布局
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
        this.adaptWindow();  // 初始时适应窗口
    }

    // 关闭窗口
    @FXML
    public void closeStage() {
        this.stage.close();

        if (helpWindow != null &&  helpWindow.controller != null) {
            helpWindow.controller.closeStage();
        }
    }

    // 最小化窗口
    @FXML
    void minimizeStage() {
        this.stage.setIconified(true);
    }

    // 记录鼠标按下时的X坐标和Y坐标
    @FXML
    void pressBar(MouseEvent event) {
        this.xOffset = event.getSceneX();
        this.yOffset = event.getSceneY();
    }

    // 根据鼠标拖动的距离，移动窗口
    @FXML
    void dragBar(MouseEvent event) {
        this.stage.setX(event.getScreenX() - this.xOffset);
        this.stage.setY(event.getScreenY() - this.yOffset);
    }

    // 调整窗口大小按钮的点击事件处理方法
    @FXML
    void resizeStage() {
        // 如果窗口大小不能调整，直接返回
        if (!this.haveResize) return;

        // 设置无法改变窗口大小
        this.haveResize = false;

        if (!this.isMax) {
            // 记录最大化前的坐标
            this.preX = this.stage.getX();
            this.preY = this.stage.getY();

            // 记录最大化前的大小
            this.preWidth = this.stage.getWidth();
            this.preHeight = this.stage.getHeight();

            // 如果窗口没有最大化，则最大化窗口
            this.stage.setWidth(this.stage.getMaxWidth());
            this.stage.setHeight(this.stage.getMaxHeight());

            // 将窗口移动到左上角
            this.stage.setX(0);
            this.stage.setY(0);
            this.adaptWindow();

            if (drawUtil != null)
                drawUtil.setCanResize(false);
        } else {
            // 如果窗口已经最大化，则还原窗口
            this.stage.setWidth(this.preWidth);
            this.stage.setHeight(this.preHeight);

            // 恢复最大化前的坐标
            this.stage.setX(this.preX);
            this.stage.setY(this.preY);
            this.adaptWindow();

            if (drawUtil != null)
                drawUtil.setCanResize(true);
        }

        // 改变窗口是否最大化的记录
        this.isMax = !this.isMax;

        // 恢复窗口大小可以改变
        this.haveResize = true;
    }

    // 调整窗口布局的方法
    public void adaptWindow() {
        double sw = this.stage.getWidth();
        this.titleBar.setPrefWidth(sw);
        this.titleBar.setMaxWidth(sw);
        this.titleBar.setMinWidth(sw);
    }

    // 将窗口显示在最前
    public void showStageToFront() {
        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();
    }


    protected HelpDialogApplication helpWindow;  // 帮助对话框应用程序
    protected boolean isFirstShow = true;  // 是否第一次显示
    @FXML
    protected void showDescription() {
        if (helpWindow == null) {
            helpWindow = new HelpDialogApplication("", 500, 500);
        }
        if (helpWindow.controller == null) {
            try {
                helpWindow.start(new Stage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Stage stage = helpWindow.controller.stage;
        if (stage.isShowing()) {
            stage.show();
            stage.setAlwaysOnTop(true);
            stage.setIconified(false);
            stage.toFront();
        }
    }
}
