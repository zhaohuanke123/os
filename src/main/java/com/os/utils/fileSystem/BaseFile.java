package com.os.utils.fileSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFile implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String Name;
    protected transient StringProperty NameP = new SimpleStringProperty();
    protected String type;
    protected int diskNum;
    protected String location;
    protected double size;
    protected String space;
    protected Date createTime;
    protected Folder parent;

    public StringProperty NamePProperty() {
        return this.NameP;
    }

    private void setNameP() {
        this.NameP.set(this.Name);
    }

    public BaseFile(String Name) {
        this.Name = Name;
        this.setNameP();
    }

    public BaseFile(String Name, String location, int diskNum, Folder parent) {
        this.Name = Name;
        this.location = location;
        this.size = 0.0;
        this.space = this.size + "B";
        this.createTime = new Date();
        this.diskNum = diskNum;
        this.type = "文件夹";
        this.parent = parent;
        this.setNameP();
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
        this.setNameP();
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

    public String toString() {
        return this.Name;
    }
}
