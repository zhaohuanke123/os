package com.os.applications.processApp.processSystem;

import com.os.utility.fileSystem.MemoryArea;
import com.os.utility.fileSystem.OccupancyManager;

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
    public ExecutableFile executableFile;
    public int memory;
    public MemoryArea memoryArea;
    public int device;
    public int deviceId;
    public int deviceRemainTime;
    public int whichFile;

    public Process(int name, ExecutableFile executableFile, int whichFile) {
        this.name = name;
        this.state = 0;
        this.PC = 0;
        this.AX = 0;
        this.pcbID = -1;
        this.executableFile = executableFile;
        this.memory = executableFile.getInstructionArray().size();
        this.memoryArea = null;
        this.device = -1;
        this.deviceId = -1;
        this.deviceRemainTime = 0;
        this.whichFile = whichFile;
    }

    public void Create() {
        if (this.pcbID == -1) {
            int newPcb;
            newPcb = OccupancyManager.applyFreePcb();
            if (newPcb == -1) {
                return;
            }

            this.pcbID = newPcb;
        }

        if (this.memoryArea == null) {
            MemoryArea newMemoryArea;
            newMemoryArea = OccupancyManager.applyMemory(this.memory);
            if (newMemoryArea == null) {
                return;
            }

            this.memoryArea = newMemoryArea;
        }

        this.state = 1;
        ProcessManager.creatingProcessList.remove(this);
        ProcessManager.waitProcessList.add(this);
    }

    public boolean Destroy() {
        this.state = -1;
        boolean isSucceed = OccupancyManager.retrieveMemory(this.memoryArea);

        System.out.println(this.memoryArea.start + " " + this.memoryArea.end);

        if (!isSucceed) {
            System.out.println("进程销毁失败，进程编号为：" + this.name);
            return false;
        } else {
            OccupancyManager.retrievePcb(this.pcbID);
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

        Instruction instruction = this.executableFile.instructionArray.get(this.PC);
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
            int deviceId = OccupancyManager.applyDevice(this.device); // 尝试申请设备
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
