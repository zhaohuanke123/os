package com.os.utility;

import com.os.dataModels.ExecutableFileData;
import com.os.dataModels.InstructionData;
import com.os.applications.processControlApp.models.ProcessData;
import com.os.applications.processControlApp.processSystem.ExeFile;
import com.os.applications.processControlApp.processSystem.Process;

import java.util.ArrayList;
import java.util.Vector;

public class DataLoader {
    public static ArrayList<ExecutableFileData> fileDataLoad(ArrayList<ExecutableFileData> data, Vector<ExeFile> exeFileList) {
        data.clear();
        for (ExeFile exeFile : exeFileList) {
            data.add(new ExecutableFileData(exeFile));
        }
        return data;
    }

    public static ArrayList<InstructionData> fileDetailDataLoad(ArrayList<InstructionData> data, ExeFile exeFile) {
        data.clear();
        for (int i = 0; i < exeFile.instructionArray.size(); ++i) {
            data.add(new InstructionData(exeFile.instructionArray.get(i), i));
        }
        return data;
    }

    public static ArrayList<ProcessData> processDetailDataLoad(ArrayList<ProcessData> data, Vector<Process> processList, String select) {
        data.clear();
        if (processList == null) return data;
        for (Process process : processList) {
            if (select.equals("当前进程")) {
                if (process.state != -1) {
                    data.add(new ProcessData(process));
                }
            } else if (select.equals("销毁进程")) {
                if (process.state == -1) {
                    data.add(new ProcessData(process));
                }
            }
        }
        return data;
    }
}
