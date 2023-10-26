package com.os.dataModels;

import com.os.applications.processControlApp.processSystem.ExeFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExecutableFileData {
   private final StringProperty fileName = new SimpleStringProperty();
   ExeFile exeFile;
   private final int id;

   public ExecutableFileData(ExeFile exeFile) {
      this.id = exeFile.id;
      this.exeFile = exeFile;
      this.setFileName(exeFile.name + exeFile.addName);
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
