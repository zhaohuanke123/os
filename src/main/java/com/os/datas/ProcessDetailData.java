package com.os.datas;

import com.os.utils.process.Process;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;

public class ProcessDetailData {
    private final StringProperty processName = new SimpleStringProperty();
    private final StringProperty processState = new SimpleStringProperty();
    private final StringProperty whichFile = new SimpleStringProperty();
    private final StringProperty havedDevice = new SimpleStringProperty();
    private final StringProperty havedMemory = new SimpleStringProperty();
    private final StringProperty havedPid = new SimpleStringProperty();
    private final StringProperty result = new SimpleStringProperty();
    private ProgressBar progressBar = new ProgressBar();

    public void setProcessName(String processName) {
        this.processName.set(processName);
    }

    public String getProcessState() {
        return this.processState.get();
    }

    public void setProcessState(String processState) {
        this.processState.set(processState);
    }

    public void setWhichFile(String whichFile) {
        this.whichFile.set(whichFile);
    }

    public void setHavedDevice(String havedDevice) {
        this.havedDevice.set(havedDevice);
    }

    public void setHavedPid(String havedPid) {
        this.havedPid.set(havedPid);
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setHavedMemory(String havedMemory) {
        this.havedMemory.set(havedMemory);
    }

    public ProcessDetailData(Process process) {
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
            if (process.memoryArea == null) {
                this.setHavedMemory("待分配");
            } else {
                this.setHavedMemory("[" + process.memoryArea.start + "," + process.memoryArea.end + "]");
            }
        } else {
            this.setHavedMemory("");
        }

        this.setResult(process.AX + "");
        double progress = (double) process.PC / (double) process.executableFile.instructionArray.size();
        this.progressBar.setProgress(progress);
        this.setProgressBar(this.progressBar);
    }
}
