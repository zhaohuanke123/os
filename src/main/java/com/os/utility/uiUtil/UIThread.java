package com.os.utility.uiUtil;

import com.os.applications.resourcesOccupancyApp.ResourcesOccupancyAppController;
import com.os.applications.processApp.ProcessAppController;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.os.applications.processApp.processSystem.ExecutableFile;
import com.os.applications.processApp.processSystem.*;
import com.os.applications.processApp.processSystem.Process;

public class UIThread extends Thread {
    public Vector<Process> runProcessList;
    public Process runProcess = null;
    public Vector<Process> creatingProcessList;
    public Vector<Process> waitProcessList;
    public Vector<Process> blockProcessList;
    public ResourcesOccupancyAppController occupancyAppController = null;
    public ProcessAppController processAppController = null;
    public int time = 0;

    public void init() {

    }

    //endregion
    public void run() {
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(ProcessManager.slice / ProcessManager.speed);
            } catch (InterruptedException var3) {
                System.out.println(Arrays.toString(var3.getStackTrace()));
            }Exception

            ++this.time;
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

            if (this.processAppController != null)
                processAppController.Update();
            if (this.occupancyAppController != null)
                occupancyAppController.Update();
        } while (true);
    }
}
