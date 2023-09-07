package com.os.utils.fileSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Folder implements Serializable {
   private static final long serialVersionUID = 1L;
   private String folderName;
   private String type;
   private int diskNum;
   private String location;
   private double size;
   private String space;
   private Date createTime;
   private Folder parent;
   private List<Object> children;
   private Path path;
   private int catalogNum;
   private transient StringProperty folderNameP = new SimpleStringProperty();

   public StringProperty folderNamePProperty() {
      return this.folderNameP;
   }

   private void setFolderNameP() {
      this.folderNameP.set(this.folderName);
   }

   public Folder(String folderName) {
      this.folderName = folderName;
      this.setFolderNameP();
   }

   public Folder(String folderName, String location, int diskNum, Folder parent) {
      this.folderName = folderName;
      this.location = location;
      this.size = 0.0;
      this.space = this.size + "B";
      this.createTime = new Date();
      this.diskNum = diskNum;
      this.type = "文件夹";
      this.parent = parent;
      this.setChildren(new ArrayList<>());
      this.catalogNum = 0;
      this.setFolderNameP();
   }

   public String getFolderName() {
      return this.folderName;
   }

   public void setFolderName(String folderName) {
      this.folderName = folderName;
      this.setFolderNameP();
   }

   public int getCatalogNum() {
      return this.catalogNum;
   }

   public void setCatalogNum(int catalogNum) {
      this.catalogNum = catalogNum;
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
   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public double getSize() {
      return this.size;
   }

   public void setSize(double KBCount) {
      this.size = KBCount;
      this.setSpace(this.size + "B");
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

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
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

   public List<Object> getChildren() {
      return this.children;
   }

   public void setChildren(List<Object> children) {
      this.children = children;
   }

   public void addChildren(Object child) {
      this.children.add(child);
   }

   public void removeChildren(Object child) {
      this.children.remove(child);
   }

   public boolean hasChild() {
      return !this.children.isEmpty();
   }

   public Path getPath() {
      return this.path;
   }

   public void setPath(Path path) {
      this.path = path;
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.folderNameP = new SimpleStringProperty(this.folderName);
   }

   public String toString() {
      return this.folderName;
   }
}
