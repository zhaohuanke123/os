package com.os.datas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.os.utils.process.ExecutableFile;

public class ExecutableFileData {
   private StringProperty fileName = new SimpleStringProperty();
   ExecutableFile executableFile;
   private int id;

   public ExecutableFileData(ExecutableFile executableFile) {
      this.id = executableFile.id;
      this.executableFile = executableFile;
      this.setFileName(executableFile.name + executableFile.addName);
   }

   public String toString() {
      return this.id + "";
   }

   public String getFileName() {
      return this.fileName.get();
   }

   public StringProperty fileNameProperty() {
      return this.fileName;
   }

   public void setFileName(String fileName) {
      this.fileName.set(fileName);
   }
}
