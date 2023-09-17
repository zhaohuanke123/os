package com.os.utils.fileSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class File implements Serializable {
   private static final long serialVersionUID = 1L;
   private String fileName;
   private String type;
   private int diskNum;
   private int flag;
   private int length;
   private String content;
   private Folder parent;
   private String location;
   private double size;
   private String space;
   private Date createTime;
   private boolean isOpen;
   private transient StringProperty fileNameP = new SimpleStringProperty();
   private transient StringProperty flagP = new SimpleStringProperty();
   private transient StringProperty diskNumP = new SimpleStringProperty();
   private transient StringProperty locationP = new SimpleStringProperty();
   private transient StringProperty lengthP = new SimpleStringProperty();

   public StringProperty fileNamePProperty() {
      return this.fileNameP;
   }

   public StringProperty flagPProperty() {
      return this.flagP;
   }

   public StringProperty diskNumPProperty() {
      return this.diskNumP;
   }

   public StringProperty locationPProperty() {
      return this.locationP;
   }

   public StringProperty lengthPProperty() {
      return this.lengthP;
   }

   public File(String fileName) {
      this.fileName = fileName;
      this.setOpened(false);
      this.setFileNameP();
      if (this.hasParent()) {
         Folder parent1 = this.getParent();
         parent1.setSize(FAT.getFolderSize(parent1));

         while(parent1.hasParent()) {
            parent1 = parent1.getParent();
            parent1.setSize(FAT.getFolderSize(parent1));
         }
      }
   }

   private void setFileNameP() {
      this.fileNameP.set(this.fileName);
   }

   private void setFlagP() {
      this.flagP.set(this.flag == 0 ? "只读" : "读写");
   }

   private void setDiskNumP() {
      this.diskNumP.set(String.valueOf(this.diskNum));
   }

   private void setLocationP() {
      this.locationP.set(this.location);
   }

   private void setLengthP() {
      this.lengthP.set(String.valueOf(this.length));
   }

   public File(String fileName, String location, int diskNum, Folder parent) {
      this.fileName = fileName;
      this.type = "文件";
      this.diskNum = diskNum;
      this.length = 0;
      this.content = "";
      this.size = getSize(0);
      this.location = location;
      this.space = this.size + "B";
      this.createTime = new Date();
      this.parent = parent;
      this.setOpened(false);
      if (this.hasParent()) {
         Folder parent1 = this.getParent();
         parent1.setSize(FAT.getFolderSize(parent1));

         while(parent1.hasParent()) {
            parent1 = parent1.getParent();
            parent1.setSize(FAT.getFolderSize(parent1));
         }
      }

      this.setFileNameP();
      this.setFlagP();
      this.setDiskNumP();
      this.setLocationP();
      this.setLengthP();
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
      this.setFileNameP();
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public int getDiskNum() {
      return this.diskNum;
   }

   public void setDiskNum(int diskNum) {
      this.diskNum = diskNum;
      this.setDiskNumP();
   }

   public int getFlag() {
      return this.flag;
   }

   public void setFlag(int flag) {
      this.flag = flag;
      this.setFlagP();
   }

   public int getLength() {
      return this.length;
   }

   public void setLength(int length) {
      this.length = length;
      this.setLengthP();
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String location) {
      this.location = location;
      this.setLocationP();
   }

   public double getSize() {
      return this.size;
   }

   public void setSize(double BCount) {
      this.size = BCount;
      this.setSpace(this.size + "B");
   }

   public static double getSize(int length) {
      return Double.parseDouble(String.valueOf(length));
   }

   public String getSpace() {
      return this.space;
   }

   public void setSpace(String space) {
      this.space = space;
   }

   public String getCreateTime() {
      SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
      return format.format(this.createTime);
   }

   public Folder getParent() {
      return this.parent;
   }

   public void setParent(Folder parent) {
      this.parent = parent;
   }

   public boolean hasParent() {
      return this.parent != null;
   }

   public boolean isOpened() {
      return this.isOpen;
   }

   public void setOpened(boolean isOpen) {
      this.isOpen = isOpen;
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.fileNameP = new SimpleStringProperty(this.fileName);
      this.flagP = new SimpleStringProperty(this.flag == 0 ? "只读" : "读写");
      this.diskNumP = new SimpleStringProperty(String.valueOf(this.type));
      this.locationP = new SimpleStringProperty(this.location);
      this.lengthP = new SimpleStringProperty(String.valueOf(this.length));
   }

   public String toString() {
      return this.fileName;
   }
}
