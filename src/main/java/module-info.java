module os {
    requires javafx.fxml;
    requires javafx.controls;

    opens com.os.applications to javafx.fxml;
    exports com.os.applications;
    opens com.os.applications.fileApp.application to javafx.fxml;
    exports com.os.applications.fileApp.application;
    opens com.os.applications.fileApp.controller to javafx.fxml;
    exports com.os.applications.fileApp.controller;
    opens com.os.applications.resourcesOccupancyApp to javafx.fxml;
    exports com.os.applications.resourcesOccupancyApp;
    opens com.os.applications.processControlApp to javafx.fxml;
    exports com.os.applications.processControlApp;
    opens com.os.dataModels to javafx.fxml;
    exports com.os.dataModels;
    opens com.os.utility to javafx.fxml;
    exports com.os.utility;
    opens com.os.main to javafx.fxml;
    exports com.os.main;
    opens com.os.utility.fileSystem to javafx.fxml;
    exports com.os.utility.fileSystem;
    opens com.os.utility.uiUtil to javafx.fxml;
    exports com.os.utility.uiUtil;
    opens com.os.applications.processControlApp.processSystem to javafx.fxml;
    exports com.os.applications.processControlApp.processSystem;
    opens com.os.applications.fileApp.fxmls to javafx.fxml;
    opens com.os.applications.fileApp.css to javafx.fxml;
    opens com.os.applications.fileApp.res to javafx.fxml;
    opens com.os.img to javafx.fxml;
    exports com.os.utility.sceneManager;
    opens com.os.utility.sceneManager to javafx.fxml;
    exports com.os;
    opens com.os to javafx.fxml;
    exports com.os.applications.processControlApp.models;
    opens com.os.applications.processControlApp.models to javafx.fxml;
    exports com.os.applications.resourcesOccupancyApp.models;
    opens com.os.applications.resourcesOccupancyApp.models to javafx.fxml;
}