package com.os.apps.helpApp;

import com.os.apps.BaseApp;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpApp extends BaseApp<HelpAppController> {
    public static void main(String[] args) {
        Application.launch(args);
    }

    public HelpApp() {
        super("/com/os/apps/helpApp/HelpApp.fxml",
                "/com/os/img/help.png",
                "帮助",
                800,
                550);
    }

    @Override
    public void start(Stage stage) throws IOException {
        super.start(stage);

        //  当窗口关闭时，退出应用程序
        stage.setOnCloseRequest(event -> System.exit(0));
    }
}
