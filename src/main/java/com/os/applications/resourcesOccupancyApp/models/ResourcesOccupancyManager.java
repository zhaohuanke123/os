package com.os.applications.resourcesOccupancyApp.models;

import java.util.Arrays;
import java.util.Vector;

/**
 * {@code @description:} 占用管理器,用于管理内存、设备、PCB
 */
public class ResourcesOccupancyManager {
    public static final int MEMORY_SIZE = 512;
    public static final int A_DEVICE_SIZE = 2;
    public static final int B_DEVICE_SIZE = 3;
    public static final int C_DEVICE_SIZE = 3;
    public static final int All_DEVICE_SIZE = A_DEVICE_SIZE + B_DEVICE_SIZE + C_DEVICE_SIZE;
    public static final int PCB_SIZE = 10;
    public static int[] allMemory = new int[MEMORY_SIZE];
    public static int[] aDevice = new int[A_DEVICE_SIZE];
    public static int[] bDevice = new int[B_DEVICE_SIZE];
    public static int[] cDevice = new int[C_DEVICE_SIZE];
    public static Vector<Integer> freePcbList = new Vector<>();

    public static void init() {
        Arrays.fill(allMemory, 0);
        Arrays.fill(aDevice, 0);
        Arrays.fill(bDevice, 0);
        Arrays.fill(cDevice, 0);

        freePcbList.clear();
        for (int i = 0; i < PCB_SIZE; ++i) {
            freePcbList.add(i);
        }

    }

    public static int applyFreePcb() {
        if (freePcbList != null && !freePcbList.isEmpty()) {
            try {
                return freePcbList.remove(0);
            } catch (Exception e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static void retrievePcb(int num) {
        for (Integer integer : freePcbList) {
            if (num == integer) {
                int a = 0;
            }
        }

        freePcbList.add(num);
    }

    // 申请内存空间
    public static MemoryBlock applyMemory(int num) {
        int series = 0;

        for (int i = 0; i < allMemory.length; ++i) {
            if (allMemory[i] == 1) {
                series = 0;
            } else {
                ++series;
            }

            if (series == num) {
                int start = i - series + 1;

                for (int j = start; j <= i; ++j) {
                    allMemory[j] = 1;
                }
                return new MemoryBlock(start, i);
            }
        }

        return null;
    }

    // 释放内存空间
    public static boolean retrieveMemory(MemoryBlock memoryBlock) {
        if (memoryBlock == null) {
            return false;
        } else {
            int start = memoryBlock.start;
            int end = memoryBlock.end;

            for (int i = start; i <= end; ++i) {
                allMemory[i] = 0;
            }

            return true;
        }
    }

    public static int getNumOfFreeMemory() {
        int num = 0;

        for (int memory : allMemory)
            if (memory == 0)
                num++;

        return num;
    }

    public static int getNumOfBusyMemory() {
        return MEMORY_SIZE - getNumOfFreeMemory();
    }

    public static int applyDevice(int device) {
        if (device == 0) {
            for (int i = 0; i < aDevice.length; i++) {
                if (aDevice[i] == 0) {
                    aDevice[i] = 1;
                    return i;
                }
            }
            return -1;
        } else if (device == 1) {
            for (int i = 0; i < bDevice.length; i++) {
                if (bDevice[i] == 0) {
                    bDevice[i] = 1;
                    return i;
                }
            }
            return -1;
        } else if (device == 2) {
            for (int i = 0; i < cDevice.length; i++) {
                if (cDevice[i] == 0) {
                    cDevice[i] = 1;
                    return i;
                }
            }
            return -1;
        }
        return -1;
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

    public static int getBusyDeviceNum() {
        int num = 0;
        for (int j : aDevice) {
            if (j == 1) {
                num++;
            }
        }
        for (int j : bDevice) {
            if (j == 1) {
                num++;
            }
        }
        for (int j : cDevice) {
            if (j == 1) {
                num++;
            }
        }
        return num;
    }

    public static boolean checkPCBIndex(int index) {
        return index >= 0 && index < PCB_SIZE;
    }
}
