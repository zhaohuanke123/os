package com.os.utility.fileSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.os.applications.processControlApp.processSystem.ExeFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Disk implements Serializable {
    private static final long serialVersionUID = 1L;
    private int num;  // 磁盘块编号
    private int index;  // 磁盘块索引
    private String type;  // 磁盘块类型
    private Object object;  // 磁盘块上存储的对象
    private boolean begin;  // 记录是否为文件的起始块
    private transient StringProperty numP = new SimpleStringProperty();
    private transient StringProperty indexP = new SimpleStringProperty();
    private transient StringProperty typeP = new SimpleStringProperty();
    private transient StringProperty objectP = new SimpleStringProperty();

    public StringProperty numPProperty() {
        return this.numP;
    }

    public StringProperty indexPProperty() {
        return this.indexP;
    }

    public StringProperty typePProperty() {
        return this.typeP;
    }

    public StringProperty objectPProperty() {
        return this.objectP;
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
        if (this.object instanceof ExeFile) {
            this.objectP.set(((ExeFile) this.object).getName());
        }

    }

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

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
        this.setNumP();
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
            this.objectP.bind(((File) object).NamePProperty());
        } else if (object instanceof Folder) {
            this.objectP.bind(((Folder) object).NamePProperty());
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

    public void allocBlock(int index, String type, Object object, boolean begin) {
        this.setIndex(index);
        this.setType(type);
        this.setObject(object);
        this.setBegin(begin);
    }

    public void clearBlock() {
        this.setIndex(0);
        this.setType("空");
        this.setObject(null);
        this.setBegin(false);
    }

    public boolean isFree() {
        return this.index == 0;
    }

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
        if (object instanceof File) {
            return object.toString();
        } else {
            return object instanceof Folder ? object.toString() : ((ExeFile) object).getName();
        }
    }
}
