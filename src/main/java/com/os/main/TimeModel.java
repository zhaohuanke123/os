package com.os.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimeModel {
    private final StringProperty time1 = new SimpleStringProperty();
    private final StringProperty time2 = new SimpleStringProperty();

    public String getTime1() {
        return time1.get();
    }

    public StringProperty time1Property() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1.set(time1);
    }

    public String getTime2() {
        return time2.get();
    }

    public StringProperty time2Property() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2.set(time2);
    }
}
