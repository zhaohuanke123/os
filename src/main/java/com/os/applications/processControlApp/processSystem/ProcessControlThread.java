package com.os.applications.processControlApp.processSystem;

import com.os.applications.processControlApp.ProcessControlAppController;
import com.os.applications.resourcesOccupancyApp.models.ResourcesOccupancyManager;
import javafx.scene.control.CheckBox;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProcessControlThread extends Thread {
    public static Vector<ExeFile> exeFileList;
    public static Vector<Integer> freePcbList;
    public static Vector<Process> allProcessList;
    public static Vector<Process> creatingProcessList;
    public static Vector<Process> runProcessList;
    public static Vector<Process> waitProcessList;
    public static Vector<Process> blockProcessList;
    public static int speed;
    public static int processNum;
    public static int slice;
    public static int sliceLength;
    public static int[] allMemory;
    public static int[] aDevice;
    public static int[] bDevice;
    public static int[] cDevice;
    public static int residueSlice = 0;
    public static CheckBox controlButton = null;
    public static ProcessControlAppController processControlAppController = null;

    public void Init() {
        sliceLength = 6;
        creatingProcessList = ProcessManager.creatingProcessList;
        exeFileList = ProcessManager.exeFileList;
        blockProcessList = ProcessManager.blockProcessList;
        waitProcessList = ProcessManager.waitProcessList;
        allProcessList = ProcessManager.allProcessList;
        runProcessList = ProcessManager.runProcessList;
        processNum = ProcessManager.processNum;
        slice = ProcessManager.slice;
        speed = ProcessManager.speed;

        freePcbList = ResourcesOccupancyManager.freePcbList;
        allMemory = ResourcesOccupancyManager.allMemory;
        aDevice = ResourcesOccupancyManager.aDevice;
        bDevice = ResourcesOccupancyManager.bDevice;
        cDevice = ResourcesOccupancyManager.cDevice;
    }

    public void CreateProcess() {
        if (controlButton == null || controlButton.isSelected()) {
            if (!exeFileList.isEmpty()) {
                if (creatingProcessList.size() < 3) {
                    Random random = new Random();
                    var executableFiles = (Vector<?>) exeFileList.clone();
                    int num = random.nextInt(executableFiles.size());
                    ExeFile exeFile = (ExeFile) executableFiles.get(num);
                    Process newProcess = new Process(processNum, exeFileList.get(num), exeFile.id);
                    creatingProcessList.add(newProcess);
                    allProcessList.add(newProcess);
                    ++processNum;
                    newProcess.Create();
                }
            }
        }
    }

    public void UpdateByRR() {
        do {
            this.CreateProcess();
            Process nowProcess;
            if (runProcessList.isEmpty() && waitProcessList != null && !waitProcessList.isEmpty()) {
                try {
                    nowProcess = waitProcessList.remove(0);
                    nowProcess.state = 2;
                    runProcessList.add(nowProcess);
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }

            if (!runProcessList.isEmpty()) {
                nowProcess = runProcessList.get(0);

                for (int i = 0; i < sliceLength; ++i) {
                    residueSlice = sliceLength - i;

                    try {
                        TimeUnit.MILLISECONDS.sleep(ProcessManager.slice / ProcessManager.speed);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    if (nowProcess.PC == nowProcess.exeFile.instructionArray.size()) {
                        nowProcess.Destroy();
                        break;
                    }

                    nowProcess.CPU();
                    if (nowProcess.state == 3) {
                        break;
                    }
                }

                if (nowProcess != null) {
                    runProcessList.remove(nowProcess);
                    if (nowProcess.state != 3 && nowProcess.state != -1) {
                        nowProcess.state = 1;
                        waitProcessList.add(nowProcess);
                    }
                }
            }
        } while (true);
    }

    public void run() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                ProcessManager.checkCreatingProcessList();
                ProcessManager.checkBlockProcessList();
            }
        }, 0L, 1000L);
        this.UpdateByRR();
    }
}
