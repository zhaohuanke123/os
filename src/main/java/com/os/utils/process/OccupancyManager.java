package com.os.utils.process;

import java.util.Vector;

public class OccupancyManager {
   public static int[] allMemory = new int[512];
   public static int[] aDevice = new int[2];
   public static int[] bDevice = new int[3];
   public static int[] cDevice = new int[3];
   public static Vector<Integer> freePcbList = new Vector();

   public static void init() {
      int i;
      for(i = 0; i < 512; ++i) {
         allMemory[i] = 0;
      }

      for(i = 0; i < 2; ++i) {
         aDevice[i] = 0;
      }

      for(i = 0; i < 2; ++i) {
         bDevice[i] = 0;
      }

      for(i = 0; i < 2; ++i) {
         cDevice[i] = 0;
      }

      for(i = 0; i < 10; ++i) {
         freePcbList.add(i);
      }

   }

   public static int applyFreePcb() {
      if (freePcbList != null && !freePcbList.isEmpty()) {
         System.out.println("pcb剩余个数：" + freePcbList.size());

         try {
            int pcb = freePcbList.remove(0);
            return pcb;
         } catch (Exception var1) {
            System.out.println("pcb剩余个数：0");
            return -1;
         }
      } else {
         return -1;
      }
   }

   public static MemoryArea applyMemory(int num) {
      int series = 0;

      for(int i = 0; i < allMemory.length; ++i) {
         if (allMemory[i] == 1) {
            series = 0;
         } else {
            ++series;
         }

         if (series == num) {
            int start = i - series + 1;
            int end = i;

            for(int j = start; j <= end; ++j) {
               allMemory[j] = 1;
            }

            System.out.println("分配内存:" + start + " " + end);
            MemoryArea memoryArea = new MemoryArea(start, end);
            return memoryArea;
         }
      }

      return null;
   }

   public static boolean retrieveMemory(MemoryArea memoryArea) {
      if (memoryArea == null) {
         return false;
      } else {
         int start = memoryArea.start;
         int end = memoryArea.end;

         for(int i = start; i <= end; ++i) {
            allMemory[i] = 0;
         }

         return true;
      }
   }

   public static void retrievePcb(int num) {
      for(int i = 0; i < freePcbList.size(); ++i) {
         if (num == freePcbList.get(i)) {
         }
      }

      freePcbList.add(num);
   }

   public static int applyDevice(int device) {
      int i;
      if (device == 0) {
         for(i = 0; i < 2 && aDevice[i] != 0; ++i) {
         }

         if (i == 2) {
            return -1;
         } else {
            aDevice[i] = 1;
            return i;
         }
      } else if (device == 1) {
         for(i = 0; i < 3 && bDevice[i] != 0; ++i) {
         }

         if (i == 3) {
            return -1;
         } else {
            bDevice[i] = 1;
            return i;
         }
      } else if (device != 2) {
         return -1;
      } else {
         for(i = 0; i < 3 && cDevice[i] != 0; ++i) {
         }

         if (i == 3) {
            return -1;
         } else {
            cDevice[i] = 1;
            return i;
         }
      }
   }

   public static void retrieveDevice(int device, int deviceId) {
      if (device == 0) {
         aDevice[deviceId] = 0;
      } else if (device == 1) {
         bDevice[deviceId] = 0;
      } else if (device == 2) {
         cDevice[deviceId] = 0;
      }

   }
}
