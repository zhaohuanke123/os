package com.os.main;

import com.os.utility.uiUtil.CompSet;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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

    public AppBoxManager(HBox appBox) {
        this.appBox = appBox;
        // layoutY 随appBox的宽度变化
    }

    public void addAppButton(Button appButton, Stage stage, String toolTipString) {
        appButtons.add(new AppBean(appButton, stage));

        appButton.setId("appButton");
        CompSet.setCompFixSize(appButton, MainController.getInstance().appWidth, 1 * MainController.getInstance().appWidth);
        CompSet.setImageViewFixSize((ImageView) appButton.getGraphic(), 0.7 * MainController.getInstance().appWidth, 0.7 * MainController.getInstance().appWidth);
        appButton.setTooltip(new Tooltip(toolTipString));

        appBox.getChildren().add(0, appButton);
        appBox.getChildren().add(0, new Separator());

        appBox.setLayoutX(MainController.getInstance().sceneWidth / 2.0 - appBox.getWidth() / 2.0);
    }

    public void removeAppButton(Stage stage) {
        for (int i = 0; i < appButtons.size(); i++) {
            appBox.getChildren().remove(0);
            appBox.getChildren().remove(0);
        }

        for (int i = 0; i < appButtons.size(); i++) {
            if (appButtons.get(i).stage.equals(stage)) {
                appButtons.remove(i);
                break;
            }
        }

        for (AppBean appButton : appButtons) {
            appBox.getChildren().add(0, appButton.appButton);
            appBox.getChildren().add(0, new Separator());
        }

        appBox.setLayoutX(MainController.getInstance().sceneWidth / 2.0 - appBox.getWidth() / 2.0);
    }

    public void updateButton() {
        appButtons.forEach((appBean -> {
            if (appBean.stage.isIconified()) {
                appBean.appButton.setId("appButton");
            } else {
                appBean.appButton.setId("appButtonSelected");
            }
        }));
    }

    static class AppBean {
        public Button appButton;
        Stage stage;

        public AppBean(Button appButton, Stage stage) {
            this.appButton = appButton;
            this.stage = stage;

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
