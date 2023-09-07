package com.os.utils;

import com.os.datas.ExecutableFileData;
import com.os.datas.InstructionData;
import com.os.datas.ProcessDetailData;
import com.os.utils.process.ExecutableFile;
import com.os.utils.process.Process;

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

      for(int i = 0; i < executableFile.instructionArray.size(); ++i) {
         data.add(new InstructionData(executableFile.instructionArray.get(i), i));
      }

      return data;
   }

   public static ArrayList<ProcessDetailData> processDetailDataLoad(ArrayList<ProcessDetailData> data, Vector<Process> processList, String select) {
      data.clear();
      if (processList == null) {
         return data;
      } else {
         for(int i = 0; i < processList.size(); ++i) {
            if (select.equals("当前进程")) {
               if (processList.get(i).state != -1) {
                  data.add(new ProcessDetailData(processList.get(i)));
               }
            } else if (select.equals("显示所有")) {
               data.add(new ProcessDetailData(processList.get(i)));
            } else if (select.equals("销毁进程")) {
               if (processList.get(i).state == -1) {
                  data.add(new ProcessDetailData(processList.get(i)));
               }
            } else if (select.equals("新建进程")) {
               if (processList.get(i).state == 0) {
                  data.add(new ProcessDetailData(processList.get(i)));
               }
            } else if (select.equals("就绪进程")) {
               if (processList.get(i).state == 1) {
                  data.add(new ProcessDetailData(processList.get(i)));
               }
            } else if (select.equals("阻塞进程")) {
               if (processList.get(i).state == 3) {
                  data.add(new ProcessDetailData(processList.get(i)));
               }
            } else if (select.equals("运行进程") && processList.get(i).state == 2) {
               data.add(new ProcessDetailData(processList.get(i)));
            }
         }

         return data;
      }
   }
}
