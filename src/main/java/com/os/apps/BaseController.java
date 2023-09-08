package com.os.apps;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BaseController {
    protected boolean haveResize = true;
    protected boolean isMax = false;
    protected Stage stage;
    @FXML
    protected BorderPane titleBar;
    protected double xOffset = 0.0;
    protected double yOffset = 0.0;

    @FXML
    protected void  closeStage(MouseEvent event) {
        this.stage.close();
    }

    @FXML
    void minimizeStage(MouseEvent event) {
        this.stage.setIconified(true);
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
    void pressBar(MouseEvent event) {
        this.xOffset = event.getSceneX();
        this.yOffset = event.getSceneY();
    }

    @FXML
    void releaseBar(MouseEvent event) {
        this.stage.setOpacity(1.0);
    }

    @FXML
    void resizeStage(MouseEvent event) {
        if (this.haveResize) {
            this.haveResize = false;
            if (!this.isMax) {
                this.stage.setWidth(this.stage.getMaxWidth());
                this.stage.setHeight(this.stage.getMaxHeight());
            } else {
                this.stage.setWidth(this.stage.getMinWidth());
                this.stage.setHeight(this.stage.getMinHeight());
            }
            this.stage.setX(0.0);
            this.stage.setY(0.0);
            this.adaptWindow();

            this.isMax = !this.isMax;
            this.haveResize = true;
        }
    }

    public void adaptWindow() {
        double sw = this.stage.getWidth();
        double sh = this.stage.getHeight();
        double w = 50.0 * sw / 1920.0;
        this.titleBar.setPrefWidth(sw);
        this.titleBar.setMaxWidth(sw);
        this.titleBar.setMinWidth(sw);
    }
}
