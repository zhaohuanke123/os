package com.os.utils.ui;

import com.os.apps.fileApp.app.MainUI;
import com.os.apps.occupancyApp.OccupancyAppController;
import com.os.apps.processApp.ProcessAppController;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.os.utils.process.ExecutableFile;
import com.os.utils.process.*;
import com.os.utils.process.Process;
import com.os.main.MainController;

public class UIThread extends Thread {
    public Vector<Process> runProcessList;
    public Process runProcess = null;
    public Vector<Process> creatingProcessList;
    public Vector<Process> waitProcessList;
    public Vector<Process> blockProcessList;
    public OccupancyAppController occupancyAppController = null;
    public ProcessAppController processAppController = null;

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


//            this.runProcessList = (Vector<Process>) ProcessManager.runProcessList.clone();
//            this.runProcess = null;
//            this.creatingProcessList = (Vector<Process>) ProcessManager.creatingProcessList.clone();
//            this.waitProcessList = (Vector<Process>) ProcessManager.waitProcessList.clone();
//            this.blockProcessList = (Vector<Process>) ProcessManager.blockProcessList.clone();
            this.runProcessList = new Vector<>(ProcessManager.runProcessList);
            this.runProcess = null;
            this.creatingProcessList = new Vector<>(ProcessManager.creatingProcessList);
            this.waitProcessList = new Vector<>(ProcessManager.waitProcessList);
            this.blockProcessList = new Vector<>(ProcessManager.blockProcessList);

            if (!this.runProcessList.isEmpty()) {
                this.runProcess = this.runProcessList.get(0);
            } else {
                Instruction instruction = new Instruction(-1);
                Vector<Instruction> instructionArrayList = new Vector<>();
                instructionArrayList.add(instruction);
                this.runProcess = new Process(-1, new ExecutableFile(-1, instructionArrayList), -1);
                this.runProcess.PC = 0;
            }

            MainController.getInstance().Update();
            if (this.processAppController != null)
                processAppController.Update();
            if (this.occupancyAppController != null)
                occupancyAppController.Update();
        } while (true);
    }
}
