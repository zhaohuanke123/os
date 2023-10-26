package com.os.main;

import com.os.utility.uiUtil.CompSet;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AppBoxManager {
    private HBox appBox;
    private ArrayList<AppBean> appButtons = new ArrayList<>();

    // 构造函数，初始化 appBox
    public AppBoxManager(HBox appBox) {
        this.appBox = appBox;
    }

    // 添加应用程序按钮到 appBox
    public void addAppButton(Button appButton, Stage stage, String toolTipString) {
        // 创建 AppBean 对象，关联应用程序按钮和窗口
        appButtons.add(new AppBean(appButton, stage));

        // 设置应用程序按钮的属性和样式
        appButton.setId("appButtonSelected");
        CompSet.setCompFixSize(appButton, MainController.getInstance().appWidth, 1 * MainController.getInstance().appWidth);
        CompSet.setImageViewFixSize((ImageView) appButton.getGraphic(), 0.7 * MainController.getInstance().appWidth, 0.7 * MainController.getInstance().appWidth);
        appButton.setTooltip(new Tooltip(toolTipString));

        // 添加应用程序按钮和分隔符到 appBox
        var size = appBox.getChildren().size();
        appBox.getChildren().add(size, appButton);
        appBox.getChildren().add(size, new Separator());

        // 调整 appBox 的布局
        appBox.setLayoutX(MainController.getInstance().sceneWidth / 2.0 - appBox.getWidth() / 2.0);
    }

    // 从 appBox 移除应用程序按钮
    public void removeAppButton(Stage stage) {
        // 移除所有应用程序按钮和分隔符
        for (int i = 0; i < appButtons.size(); i++) {
            appBox.getChildren().remove(appBox.getChildren().size() - 1);
            appBox.getChildren().remove(appBox.getChildren().size() - 1);
        }

        // 从 appButtons 列表中移除对应的 AppBean 对象
        for (int i = 0; i < appButtons.size(); i++) {
            if (appButtons.get(i).stage.equals(stage)) {
                appButtons.remove(i);
                break;
            }
        }

        // 重新添加应用程序按钮和分隔符
        for (AppBean appButton : appButtons) {
            appBox.getChildren().add(appBox.getChildren().size(),appButton.appButton);
            appBox.getChildren().add(appBox.getChildren().size(),new Separator());
        }

        // 调整 appBox 的布局
        appBox.setLayoutX(MainController.getInstance().sceneWidth / 2.0 - appBox.getWidth() / 2.0);
    }

    static class AppBean {
        public Button appButton;
        Stage stage;

        // 构造函数，初始化 AppBean 对象，关联应用程序按钮和窗口
        public AppBean(Button appButton, Stage stage) {
            this.appButton = appButton;
            this.stage = stage;

            // 处理应用程序按钮的点击事件，最小化或还原窗口
            appButton.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (!stage.isIconified()) {
                        stage.setIconified(true);
                    } else if (stage.isIconified()) {
                        stage.setIconified(false);
                        stage.show();
                        stage.toFront();
                    }
                }
            });
        }
    }
}
