module os {
    requires javafx.fxml;
    requires javafx.controls;

    opens com.os.apps to javafx.fxml;
    exports com.os.apps;
    opens com.os.apps.fileApp.app to javafx.fxml;
    exports com.os.apps.fileApp.app;
    opens com.os.apps.fileApp.Controller to javafx.fxml;
    exports com.os.apps.fileApp.Controller;
    opens com.os.apps.occupancyApp to javafx.fxml;
    exports com.os.apps.occupancyApp;
    opens com.os.apps.helpApp to javafx.fxml;
    exports com.os.apps.helpApp;
    opens com.os.apps.processApp to javafx.fxml;
    exports com.os.apps.processApp;
    opens com.os.apps.systemFileApp to javafx.fxml;
    exports com.os.apps.systemFileApp;
    opens com.os.datas to javafx.fxml;
    exports com.os.datas;
    opens com.os.utils to javafx.fxml;
    exports com.os.utils;
    opens com.os.main to javafx.fxml;
    exports com.os.main;
    opens com.os.utils.fileSystem to javafx.fxml;
    exports com.os.utils.fileSystem;
    opens com.os.utils.ui to javafx.fxml;
    exports com.os.utils.ui;
    opens com.os.utils.process to javafx.fxml;
    exports com.os.utils.process;
    opens com.os.apps.fileApp.fxmls to javafx.fxml;
    opens com.os.apps.fileApp.css to javafx.fxml;
    opens com.os.apps.fileApp.res to javafx.fxml;
    opens com.os.img to javafx.fxml;
}