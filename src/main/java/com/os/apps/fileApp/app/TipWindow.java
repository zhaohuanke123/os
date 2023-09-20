package com.os.apps.fileApp.app;

import com.os.apps.BaseApp;
import com.os.apps.fileApp.Controller.TipWindowCtl;
import javafx.stage.Stage;

import java.io.IOException;

public class TipWindow extends BaseApp<TipWindowCtl> {
    String tipString;

    public static void main(String[] args) {
        launch(args);
    }

    public TipWindow(String tipString) {
        super(
                "/com/os/apps/fileApp/fxmls/TipWindow.fxml",
                "/com/os/apps/fileApp/res/tip.png",
                "提示",
                -1,
                -1
        );

        this.tipString = tipString;
    }

    public void start(Stage stage) throws IOException {
        super.start(stage);

        controller.init(stage,tipString);
    }
}
