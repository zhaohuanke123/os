package com.os;

import com.os.apps.helpApp.HelpApp;
import com.os.apps.occupancyApp.OccupancyApp;
import com.os.apps.systemFileApp.SystemFileApp;
import com.os.main.MainWindow;

public class StartMain {
    public static void main(String[] args) {
        MainWindow.main(args);
    }
}

class StartSystemFile {
    public static void main(String[] args) {
        SystemFileApp.main(args);
    }
}

class StartHelpApp {
    public static void main(String[] args) {
        HelpApp.main(args);
    }
}

class StartOccupancy {
    public static void main(String[] args) {
        OccupancyApp.main(args);
    }
}