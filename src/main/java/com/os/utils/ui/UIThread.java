package com.os.utils.ui;

import com.os.apps.fileApp.app.MainUI;
import com.os.apps.occupancyApp.OccupancyAppController;
import com.os.apps.processApp.ProcessAppController;
import com.os.datas.InstructionData;
import com.os.datas.ProcessDetailData;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.os.utils.DataLoader;
import com.os.utils.fileSystem.FAT;
import com.os.utils.process.ExecutableFile;
import com.os.utils.process.*;
import com.os.utils.process.Process;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.os.main.MainController;

public class UIThread extends Thread {
    public static ArrayList<ProcessDetailData> processDetailDataArrayList = new ArrayList<>();
    public static ArrayList<InstructionData> instructionDataArrayList = new ArrayList<>();
    public Vector<Process> runProcessList;
//    public static Button timeButton1 = null;
//    public static Button timeButton2 = null;
//    public static TableView<ProcessDetailData> processTable = null;
//    public static TableView<InstructionData> nowProcessTable = null;
//    public static Button nowProcessName = null;
//    public static Button nowResult = null;
//    public static Button residueSlice = null;
    public Process runProcess = null;
    public Vector<Process> creatingProcessList;
    public Vector<Process> waitProcessList;
    public Vector<Process> blockProcessList;
//    public static Button nowInstruction = null;
//    public static CheckBox[] checkBoxes1 = null;
//    public static CheckBox[] checkBoxes2 = null;
    //    public static VBox[] boxes1 = null;
//    public static HBox[] boxes2 = null;
//    public static Button[] textButtons = null;
    public OccupancyAppController occupancyAppController = null;
    public ProcessAppController processAppController = null;
//    public static Button[] mainButtons = null;
//    public static Vector<StageRecord> stageList = null;

    public void init() {
    }

    //endregion
    public void run() {
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(ProcessManager.slice / ProcessManager.speed);
            } catch (InterruptedException var3) {
                System.out.println(Arrays.toString(var3.getStackTrace()));
            }

//            this.mainButtonsUpdate();
            MainController.getInstance().appButtonUpdate();

            this.runProcessList = (Vector<Process>) ProcessManager.runProcessList.clone();
            this.runProcess = null;
            this.creatingProcessList = (Vector<Process>) ProcessManager.creatingProcessList.clone();
            this.waitProcessList = (Vector<Process>) ProcessManager.waitProcessList.clone();
            this.blockProcessList = (Vector<Process>) ProcessManager.blockProcessList.clone();

            if (!this.runProcessList.isEmpty()) {
                this.runProcess = this.runProcessList.get(0);
            } else {
                Instruction instruction = new Instruction(-1);
                Vector<Instruction> instructionArrayList = new Vector<>();
                instructionArrayList.add(instruction);
                this.runProcess = new Process(-1, new ExecutableFile(-1, instructionArrayList), -1);
                this.runProcess.PC = 0;
            }

            MainController.getInstance().timeUpdate();
            if (this.processAppController != null)
                processAppController.ProcessUpdate();
            if (this.occupancyAppController != null)
                occupancyAppController.occupancyAppUpdate();
        } while (true);
    }
}
