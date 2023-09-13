package com.os.datas;

import com.os.utils.process.ExecutableFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExecutableFileData {
   private final StringProperty fileName = new SimpleStringProperty();
   ExecutableFile executableFile;
   private final int id;

   public ExecutableFileData(ExecutableFile executableFile) {
      this.id = executableFile.id;
      this.executableFile = executableFile;
      this.setFileName(executableFile.name + executableFile.addName);
   }

   public String toString() {
      return this.id + "";
   }

   public void setFileName(String fileName) {
      this.fileName.set(fileName);
   }
}
