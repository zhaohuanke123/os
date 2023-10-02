package com.os.utils.fileSystem;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Folder extends BaseFile implements Serializable {
    private List<Object> children;
    private Path path;
    private int catalogNum;

    public Folder(String folderName) {
        super(folderName);
    }

    public Folder(String folderName, String location, int diskNum, Folder parent) {
        super(folderName, location, diskNum, parent);
        this.setChildren(new ArrayList<>());
        this.catalogNum = 0;
    }

    public int getCatalogNum() {
        return this.catalogNum;
    }

    public void setCatalogNum(int catalogNum) {
        this.catalogNum = catalogNum;
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
        this.NameP = new SimpleStringProperty(this.Name);
    }

}
