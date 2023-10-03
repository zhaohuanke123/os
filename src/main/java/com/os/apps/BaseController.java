package com.os.apps;

import com.os.utility.uiUtil.DrawUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BaseController {
    protected boolean haveResize = true;  // 是否允许窗口调整大小
    protected boolean isMax = false;  // 窗口是否已最大化
    protected Stage stage;  // 窗口对象
    protected double xOffset = 0.0;  // 鼠标拖动时的X偏移
    protected double yOffset = 0.0;  // 鼠标拖动时的Y偏移

    @FXML
    protected BorderPane titleBar;  // 标题栏界面组件
    @FXML
    protected HBox titleBarL;
    @FXML
    protected HBox titleBarR;
    @FXML
    public Label title;
    @FXML
    protected AnchorPane topMainPane;


    // 初始化方法，用于设置窗口对象
    public void init(Stage stage) {
        this.stage = stage;

        DrawUtil drawUtil = new DrawUtil();
        drawUtil.addDrawFunc(stage, this.topMainPane);
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::adaptWindow));
        stage.heightProperty().addListener((observable, oldValue, newValue)
                -> Platform.runLater(this::adaptWindow));
        this.adaptWindow();
    }

    // 关闭窗口
    @FXML
    public void closeStage() {
        this.stage.close();
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

    // 拖动完成后恢复窗口不透明度
    @FXML
    void dragBarDone() {
        this.stage.setOpacity(1.0);
    }

    // 鼠标释放后恢复窗口不透明度
    @FXML
    void releaseBar() {
        this.stage.setOpacity(1.0);
    }

    // 调整窗口大小按钮的点击事件处理方法
    @FXML
    void resizeStage() {
        // 如果窗口大小不能调整，直接返回
        if (!this.haveResize) return;

        // 设置无法改变窗口大小
        this.haveResize = false;

        if (!this.isMax) {
            // 如果窗口没有最大化，则最大化窗口
            this.stage.setWidth(this.stage.getMaxWidth());
            this.stage.setHeight(this.stage.getMaxHeight());
        } else {
            // 如果窗口已经最大化，则还原窗口
            this.stage.setWidth(this.stage.getMinWidth());
            this.stage.setHeight(this.stage.getMinHeight());
        }

        // 调整窗口布局
        this.stage.setX(0.0);
        this.stage.setY(0.0);
        this.adaptWindow();

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

    public void showStageToFront() {
        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();
    }
}
