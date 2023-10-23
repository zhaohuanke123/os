package com.os.applications.fileApp.application;

import com.os.applications.fileApp.controller.TipDialogController;
import javafx.stage.Stage;

import java.io.IOException;

public class TipDialogApplication extends BaseFileApplication<TipDialogController> {
    String tipString;

    public static void main(String[] args) {
        launch(args);
    }

    public TipDialogApplication(String tipString) {
        super(
                "/com/os/applications/fileApp/fxmls/TipDialog.fxml",
                "/com/os/applications/fileApp/res/tip.png",
                "提示",
                -1,
                -1
        );

        this.tipString = tipString;
        super.toolTip = tipString;
    }

    public TipDialogApplication(String tipString, int width, int height) {
        super(
                "/com/os/applications/fileApp/fxmls/TipDialog.fxml",
                "/com/os/applications/fileApp/res/tip.png",
                "提示",
                width,
                height
        );

        this.tipString = tipString;
        super.toolTip = tipString;
    }

    public void start(Stage stage) throws IOException {
        super.start(stage);

        controller.init(stage, tipString);

        stage.setOnCloseRequest(event -> {
            controller.closeStage();
        });
    }
}
