package com.os.utils.fileSystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class File extends BaseFile implements Serializable {
    private int flag;
    private String content;
    private boolean isOpen;
    private transient StringProperty flagP = new SimpleStringProperty();
    private transient StringProperty diskNumP = new SimpleStringProperty();
    private transient StringProperty locationP = new SimpleStringProperty();

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
        super(fileName);
        this.setOpened(false);
        if (this.hasParent()) {
            Folder parent1 = this.getParent();
            parent1.setSize(FAT.getFolderSize(parent1));

            while (parent1.hasParent()) {
                parent1 = parent1.getParent();
                parent1.setSize(FAT.getFolderSize(parent1));
            }
        }
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

    public File(String fileName, String location, int diskNum, Folder parent) {
        super(fileName, location, diskNum, parent);

        content = "";

        this.setOpened(false);
        if (this.hasParent()) {
            Folder parent1 = this.getParent();
            parent1.setSize(FAT.getFolderSize(parent1));

            while (parent1.hasParent()) {
                parent1 = parent1.getParent();
                parent1.setSize(FAT.getFolderSize(parent1));
            }
        }

        this.setFlagP();
        this.setDiskNumP();
        this.setLocationP();
        this.setLengthP();
    }

    public void setDiskNum(int diskNum) {
        super.setDiskNum(diskNum);
        this.setDiskNumP();
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
        this.setFlagP();
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLocation(String location) {
        super.setLocation(location);
        this.setLocationP();
    }

    public static double getSize(int length) {
        return Double.parseDouble(String.valueOf(length));
    }

    public boolean isOpened() {
        return this.isOpen;
    }

    public void setOpened(boolean isOpen) {
        this.isOpen = isOpen;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.NameP = new SimpleStringProperty(this.Name);
        this.flagP = new SimpleStringProperty(this.flag == 0 ? "只读" : "读写");
        this.diskNumP = new SimpleStringProperty(String.valueOf(this.type));
        this.locationP = new SimpleStringProperty(this.location);
        this.lengthP = new SimpleStringProperty(String.valueOf(this.length));
    }

}
