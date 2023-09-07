package com.os.utils.process;

import java.util.Vector;

public class ProcessManager {
   public static Vector<ExecutableFile> executableFileList = new Vector<>();
   public static Vector<Process> allProcessList = new Vector<>();
   public static Vector<Process> creatingProcessList = new Vector<>();
   public static Vector<Process> runProcessList = new Vector<>();
   public static Vector<Process> waitProcessList = new Vector<>();
   public static Vector<Process> blockProcessList = new Vector<>();
   public static int slice = 1000;
   public static int speed = 1;
   public static int processNum = 0;

   public static void init() {
      createRandomExecuteFile(executableFileList, 10);
   }

   public static void createRandomExecuteFile(Vector<ExecutableFile> executeFileList, int num) {
      for(int i = 0; i < num; ++i) {
         ExecutableFile executableFile = new ExecutableFile(i);
         executeFileList.add(executableFile);
      }

   }

   public static void checkCreatingProcessList() {
       for (Process process : allProcessList) {
           if (process.state == 0) {
               process.Create();
           }
       }

   }

   public static void checkBlockProcessList() {
       for (Process process : allProcessList) {
           if (process.state == 3) {
               if (process.deviceId == -1) {
                   int deviceId = OccupancyManager.applyDevice(process.device);
                   if (deviceId != -1) {
                       process.deviceId = deviceId;
                   }
               } else {
                   useDevice(process);
               }
           }
       }

   }

    /**
     *  模拟使用设备
     * @param process  进程
     */
   public static void useDevice(Process process) {
      if (process.deviceRemainTime == 0) {
         OccupancyManager.retrieveDevice(process.device, process.deviceId);
         process.Awake();
      } else {
         --process.deviceRemainTime;
      }
   }
}
