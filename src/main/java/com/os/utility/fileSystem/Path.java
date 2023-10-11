package com.os.utility.fileSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Path implements Serializable {
   private static final long serialVersionUID = 1L;
   private String pathName;  // 路径名称
   private Path parent;  // 父路径
   private List<Path> children;  // 子路径

   public Path(String name, Path parent) {
      this.setPathName(name);
      this.setParent(parent);
      this.children = new ArrayList<>();
   }

   public String getPathName() {
      return this.pathName;
   }

   public void setPathName(String pathName) {
      this.pathName = pathName;
   }

   public Path getParent() {
      return this.parent;
   }

   public void setParent(Path parent) {
      this.parent = parent;
   }

   public boolean hasParent() {
      return this.parent != null;
   }

   public List<Path> getChildren() {
      return this.children;
   }

   public void setChildren(List<Path> children) {
      this.children = children;
   }

   public void addChildren(Path child) {
      this.children.add(child);
   }

   public void removeChildren(Path child) {
      this.children.remove(child);
   }

   public boolean hasChild() {
      return !this.children.isEmpty();
   }

   public String toString() {
      return "Path [pathName=" + this.pathName + "]";
   }
}
