package com.os.applications.fileApp.application;

import com.os.applications.fileApp.controller.HelpDialogController;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpDialogApplication extends BaseFileApplication<HelpDialogController> {
    public static void main(String[] args) {
        launch(args);
    }

    public HelpDialogApplication(String tipString, int width, int height) {
        super("/com/os/applications/fileApp/fxmls/HelpDialog.fxml",
                "/com/os/applications/fileApp/res/help.png",
                "帮助",
                width,
                height);

        super.toolTip = "帮助";
    }

    public void start(Stage stage) throws IOException {
        super.start(stage);
        controller.init(stage);
        stage.setOnCloseRequest(event -> controller.closeStage());
    }
}
