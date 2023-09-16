package com.os.utils.fileSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.os.utils.process.ExecutableFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Disk implements Serializable {
   private static final long serialVersionUID = 1L;
   private final int num;  // 磁盘编号
   private int index;  // 下一个磁盘编号
   private String type;  // 存储类型
   private Object object;
   private boolean begin;  // 是否为开端
   private transient StringProperty numP = new SimpleStringProperty();
   private transient StringProperty indexP = new SimpleStringProperty();
   private transient StringProperty typeP = new SimpleStringProperty();
   private transient StringProperty objectP = new SimpleStringProperty();

   public Disk(int num, int index, String type, Object object) {
      this.num = num;
      this.index = index;
      this.type = type;
      this.object = object;
      this.begin = false;
      this.setNumP();
      this.setIndexP();
      this.setTypeP();
      this.setObjectP();
   }

   private void setNumP() {
      this.numP.set(String.valueOf(this.num));
   }

   private void setIndexP() {
      this.indexP.set(String.valueOf(this.index));
   }

   private void setTypeP() {
      this.typeP.set(this.type);
   }

   private void setObjectP() {
      this.objectP.set(this.object == null ? "" : this.object.toString());
      if (this.object instanceof ExecutableFile) {
         this.objectP.set(((ExecutableFile)this.object).getName());
      }
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
      this.setIndexP();
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
      this.setTypeP();
   }

   public Object getObject() {
      return this.object;
   }

   public void setObject(Object object) {
      this.object = object;
      if (object instanceof File) {
         this.objectP.bind(((File)object).fileNamePProperty());
      } else if (object instanceof Folder) {
         this.objectP.bind(((Folder)object).folderNamePProperty());
      } else {
         this.objectP.unbind();
         this.setObjectP();
      }
   }

   public boolean getBegin() {
      return this.begin;
   }

   public void setBegin(boolean begin) {
      this.begin = begin;
   }

   // 分配磁盘
   public void allocBlock(int index, String type, Object object, boolean begin) {
      this.setIndex(index);
      this.setType(type);
      this.setObject(object);
      this.setBegin(begin);
   }

   // 清空磁盘
   public void clearBlock() {
      this.setIndex(0);
      this.setType("空");
      this.setObject(null);
      this.setBegin(false);
   }

   // 判断磁盘是否空闲
   public boolean isFree() {
      return this.index == 0;
   }

   // 读取数据
   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.numP = new SimpleStringProperty(String.valueOf(this.num));
      this.indexP = new SimpleStringProperty(String.valueOf(this.index));
      this.typeP = new SimpleStringProperty(this.type);
      this.objectP = new SimpleStringProperty(this.object == null ? "" : this.object.toString());
      this.setObject(this.object);
   }

   public String toString() {
      Object object = this.getObject();
      // object是文件
      if (object instanceof File) return object.toString();
      // object是目录
      else if(object instanceof Float) return object.toString();
      // object是可执行文件
      return ((ExecutableFile)object).getName();
   }
}
