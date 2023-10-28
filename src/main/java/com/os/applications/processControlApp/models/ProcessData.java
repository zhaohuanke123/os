package com.os.applications.processControlApp.models;

import com.os.applications.processControlApp.processSystem.Process;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;

import java.util.Date;

public class ProcessData {
    private final StringProperty processName = new SimpleStringProperty();
    private final StringProperty processState = new SimpleStringProperty();
    private final StringProperty whichFile = new SimpleStringProperty();
    private final StringProperty havedDevice = new SimpleStringProperty();
    private final StringProperty havedMemory = new SimpleStringProperty();
    private final StringProperty havedPid = new SimpleStringProperty();
    private final StringProperty result = new SimpleStringProperty();
    private final StringProperty finishTime = new SimpleStringProperty();
    private ProgressBar progressBar = new ProgressBar();

    public String getProcessName() {
        return this.processName.get();
    }

    public StringProperty processNameProperty() {
        return this.processName;
    }

    public void setProcessName(String processName) {
        this.processName.set(processName);
    }

    public String getProcessState() {
        return this.processState.get();
    }

    public StringProperty processStateProperty() {
        return this.processState;
    }

    public void setProcessState(String processState) {
        this.processState.set(processState);
    }

    public String getWhichFile() {
        return this.whichFile.get();
    }

    public StringProperty whichFileProperty() {
        return this.whichFile;
    }

    public void setWhichFile(String whichFile) {
        this.whichFile.set(whichFile);
    }

    public String getHavedDevice() {
        return this.havedDevice.get();
    }

    public StringProperty havedDeviceProperty() {
        return this.havedDevice;
    }

    public void setHavedDevice(String havedDevice) {
        this.havedDevice.set(havedDevice);
    }

    public String getHavedPid() {
        return this.havedPid.get();
    }

    public StringProperty havedPidProperty() {
        return this.havedPid;
    }

    public void setHavedPid(String havedPid) {
        this.havedPid.set(havedPid);
    }

    public String getResult() {
        return this.result.get();
    }

    public StringProperty resultProperty() {
        return this.result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public String getHavedMemory() {
        return this.havedMemory.get();
    }

    public StringProperty havedMemoryProperty() {
        return this.havedMemory;
    }

    public void setHavedMemory(String havedMemory) {
        this.havedMemory.set(havedMemory);
    }

    public ProcessData(Process process) {
        this.setProcessName(process.name + "");
        if (process.state == 0) {
            this.setProcessState("新建态");
        } else if (process.state == 1) {
            this.setProcessState("就绪态");
        } else if (process.state == 2) {
            this.setProcessState("运行态");
        } else if (process.state == 3) {
            this.setProcessState("阻塞态");
        } else if (process.state == -1) {
            this.setProcessState("已销毁");
        }

        this.setWhichFile("可执行文件" + process.whichFile);
        if (process.state != 3) {
            this.setHavedDevice("");
        } else {
            String s;
            if (process.device == 0) {
                s = "A";
            } else if (process.device == 1) {
                s = "B";
            } else {
                s = "C";
            }

            if (process.deviceId == -1) {
                this.setHavedDevice("等待设备" + s);
            } else {
                this.setHavedDevice("占用设备" + s);
            }
        }

        if (process.state != -1 && process.state != 0) {
            this.setHavedPid(process.pcbID + "");
        } else {
            this.setHavedPid("");
        }

        if (process.state != -1) {
            if (process.memoryBlock == null) {
                this.setHavedMemory("待分配");
            } else {
                this.setHavedMemory("[" + process.memoryBlock.start + "," + process.memoryBlock.end + "]");
            }
        } else {
            this.setHavedMemory("");
        }

        this.setResult(process.AX + "");
        double progress = (double) process.PC / (double) process.exeFile.instructionArray.size();
        this.progressBar.setProgress(progress);
        this.setProgressBar(this.progressBar);

        this.finishTime.set(process.finishTime);
    }

    public String getFinishTime() {
        return finishTime.get();
    }

    public StringProperty finishTimeProperty() {
        return finishTime;
    }
}
