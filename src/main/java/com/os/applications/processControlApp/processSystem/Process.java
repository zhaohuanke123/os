package com.os.applications.processControlApp.processSystem;

import com.os.applications.resourcesOccupancyApp.models.MemoryBlock;
import com.os.applications.resourcesOccupancyApp.models.ResourcesOccupancyManager;

import java.util.Date;

public class Process {
    public int name;
    /**
     * 进程的状态
     * 0：创建中
     * 1：就绪
     * 2：运行
     * 3：阻塞
     * -1：销毁
     */
    public int state;
    public int PC;
    public int AX;
    public int pcbID;
    public ExeFile exeFile;
    public int memory;
    public MemoryBlock memoryBlock;
    public int device;
    public int deviceId;
    public int deviceRemainTime;
    public int whichFile;
    public String finishTime;

    public Process(int name, ExeFile exeFile, int whichFile) {
        this.name = name;
        this.state = 0;
        this.PC = 0;
        this.AX = 0;
        this.pcbID = -1;
        this.exeFile = exeFile;
        this.memory = exeFile.getInstructionArray().size();
        this.memoryBlock = null;
        this.device = -1;
        this.deviceId = -1;
        this.deviceRemainTime = 0;
        this.whichFile = whichFile;
    }

    public void Create() {
        if (this.pcbID == -1) {
            int newPcb;
            newPcb = ResourcesOccupancyManager.applyFreePcb();
            if (newPcb == -1) {
                return;
            }

            this.pcbID = newPcb;
        }

        if (this.memoryBlock == null) {
            MemoryBlock newMemoryBlock;
            newMemoryBlock = ResourcesOccupancyManager.applyMemory(this.memory);
            if (newMemoryBlock == null) {
                return;
            }

            this.memoryBlock = newMemoryBlock;
        }

        this.state = 1;
        ProcessManager.creatingProcessList.remove(this);
        ProcessManager.waitProcessList.add(this);
    }

    public boolean Destroy() {
        this.state = -1;
        boolean isSucceed = ResourcesOccupancyManager.retrieveMemory(this.memoryBlock);
        Date date = new Date();
        finishTime = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

        if (!isSucceed) {
            return false;
        } else {
            ResourcesOccupancyManager.retrievePcb(this.pcbID);
            isSucceed = ProcessManager.runProcessList.remove(this);
            return isSucceed;
        }
    }

    public void Block() {
        this.state = 3;
        ProcessManager.runProcessList.remove(this);
        ProcessManager.blockProcessList.add(this);
    }

    public void Awake() {
        this.state = 1;
        this.device = -1;
        this.deviceId = -1;
        ProcessManager.blockProcessList.remove(this);
        ProcessManager.waitProcessList.add(this);
    }

    /**
     * 进程执行
     */
    public int CPU() {
        this.state = 2;

        Instruction instruction = this.exeFile.instructionArray.get(this.PC);
        ++this.PC;

        if (instruction.category == 0) {
            this.AX = instruction.operand0;
            return 0;
        } else if (instruction.category == 1) {
            ++this.AX;
            return 0;
        } else if (instruction.category == 2) {
            --this.AX;
            return 0;
        } else if (instruction.category == 3) {
            this.device = instruction.operand0;
            this.deviceRemainTime = instruction.operand1;
            int deviceId = ResourcesOccupancyManager.applyDevice(this.device); // 尝试申请设备
            if (deviceId == -1) { // 申请失败
                this.Block();
            } else { // 申请成功
                this.deviceId = deviceId;
                this.Block();
                ProcessManager.useDevice(this);
            }

            return 1;
        }

        return 0;
    }
}
