package com.os.utility;

import com.os.dataModels.ExecutableFileData;
import com.os.dataModels.InstructionData;
import com.os.applications.processApp.models.ProcessDetailData;
import com.os.applications.processApp.processSystem.ExecutableFile;
import com.os.applications.processApp.processSystem.Process;

import java.util.ArrayList;
import java.util.Vector;

public class DataLoader {
    public static ArrayList<ExecutableFileData> fileDataLoad(ArrayList<ExecutableFileData> data, Vector<ExecutableFile> executableFileList) {
        data.clear();
        for (ExecutableFile executableFile : executableFileList) {
            data.add(new ExecutableFileData(executableFile));
        }
        return data;
    }

    public static ArrayList<InstructionData> fileDetailDataLoad(ArrayList<InstructionData> data, ExecutableFile executableFile) {
        data.clear();
        for (int i = 0; i < executableFile.instructionArray.size(); ++i) {
            data.add(new InstructionData(executableFile.instructionArray.get(i), i));
        }
        return data;
    }

    public static ArrayList<ProcessDetailData> processDetailDataLoad(ArrayList<ProcessDetailData> data, Vector<Process> processList, String select) {
        data.clear();
        if (processList == null) return data;
        for (Process process : processList) {
            if (select.equals("当前进程")) {
                if (process.state != -1) {
                    data.add(new ProcessDetailData(process));
                }
            } else if (select.equals("销毁进程")) {
                if (process.state == -1) {
                    data.add(new ProcessDetailData(process));
                }
            }
        }
        return data;
    }
}
