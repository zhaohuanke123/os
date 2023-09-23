package com.os.utils;

import java.util.Vector;

public class Test {
    public static void main(String[] args) {
        Vector<Process> a = new Vector<>();
        Vector<Process> b = new Vector<>(a);
        System.out.println(a == b);
    }
}
