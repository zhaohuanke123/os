package com.os.utils.process;

import javafx.scene.control.CheckBox;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class ProcessScheduleThread extends Thread {
    public static Vector<ExecutableFile> executableFileList;
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
    public static CheckBox[] controlButton = null;

    public void Init() {
        sliceLength = 6;
        creatingProcessList = ProcessManager.creatingProcessList;
        executableFileList = ProcessManager.executableFileList;
        blockProcessList = ProcessManager.blockProcessList;
        waitProcessList = ProcessManager.waitProcessList;
        allProcessList = ProcessManager.allProcessList;
        runProcessList = ProcessManager.runProcessList;
        processNum = ProcessManager.processNum;
        slice = ProcessManager.slice;
        speed = ProcessManager.speed;

        freePcbList = OccupancyManager.freePcbList;
        allMemory = OccupancyManager.allMemory;
        aDevice = OccupancyManager.aDevice;
        bDevice = OccupancyManager.bDevice;
        cDevice = OccupancyManager.cDevice;
    }

    public void CreateProcess() {
        if (controlButton == null || controlButton.length < 2 || !controlButton[1].isSelected()) {
            if (!executableFileList.isEmpty()) {
                if (creatingProcessList.size() < 3) {
                    Random random = new Random();
                    var executableFiles = (Vector<?>) executableFileList.clone();
                    int num = random.nextInt(executableFiles.size());
                    ExecutableFile executableFile = (ExecutableFile) executableFiles.get(num);
                    int processId = processNum;
                    Process newProcess = new Process(processNum, executableFileList.get(num), executableFile.id);
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
                System.out.println("就绪队列个数：" + waitProcessList.size());

                try {
                    nowProcess = waitProcessList.remove(0);
                    nowProcess.state = 2;
                    runProcessList.add(nowProcess);
                } catch (Exception var5) {
                    System.out.println("就绪队列个数：0");
                }
            }

            if (!runProcessList.isEmpty()) {
                nowProcess = runProcessList.get(0);

                for (int i = 0; i < sliceLength; ++i) {
                    residueSlice = sliceLength - i;

                    try {
                        TimeUnit.MILLISECONDS.sleep(ProcessManager.slice / ProcessManager.speed);
                    } catch (InterruptedException var4) {
                        System.out.println(var4.getMessage());
                    }

                    if (nowProcess.PC == nowProcess.executableFile.instructionArray.size()) {
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
