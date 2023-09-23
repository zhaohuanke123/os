package com.os.utils.ui;

import java.util.Vector;

public class CopyVector<T> {

    public Vector<T> copy(Vector<T> vector) {
        return new Vector<>(vector);
    }
}
