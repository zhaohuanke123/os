package com.os.applications.processControlApp.processSystem;

import com.os.applications.resourcesOccupancyApp.models.OccupancyManager;

import java.util.Vector;

public class ProcessManager {
   public static Vector<ExeFile> exeFileList = new Vector<>();
   public static Vector<Process> allProcessList = new Vector<>();
   public static Vector<Process> creatingProcessList = new Vector<>();
   public static Vector<Process> runProcessList = new Vector<>();
   public static Vector<Process> waitProcessList = new Vector<>();
   public static Vector<Process> blockProcessList = new Vector<>();
   public static int slice = 1000;
   public static int speed = 1;
   public static int processNum = 0;

   public static void init() {
      createRandomExecuteFile(exeFileList, 10);
   }

   public static void createRandomExecuteFile(Vector<ExeFile> executeFileList, int num) {
      for(int i = 0; i < num; ++i) {
         ExeFile exeFile = new ExeFile(i);
         executeFileList.add(exeFile);
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
