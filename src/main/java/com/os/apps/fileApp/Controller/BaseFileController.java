package com.os.apps.fileApp.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BaseFileController {

    protected double xOffset = 0.0;
    protected double yOffset = 0.0;
    protected Stage stage;

    @FXML
    void closeStage(MouseEvent event) {
        this.stage.close();
    }

    @FXML
    void minimizeStage(MouseEvent event) {
        this.stage.setIconified(true);
    }

    @FXML
    void pressBar(MouseEvent event) {
        this.xOffset = event.getSceneX();
        this.yOffset = event.getSceneY();
    }

    @FXML
    void dragBar(MouseEvent event) {
        this.stage.setX(event.getScreenX() - this.xOffset);
        this.stage.setY(event.getScreenY() - this.yOffset);
    }

    @FXML
    void dragBarDone(DragEvent event) {
        this.stage.setOpacity(1.0);
    }

    @FXML
    void releaseBar(MouseEvent event) {
        this.stage.setOpacity(1.0);
    }
}
