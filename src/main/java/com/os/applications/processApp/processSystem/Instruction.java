package com.os.applications.processApp.processSystem;

import java.io.Serializable;

public class Instruction implements Serializable {
    /**
     * 指令类型
     * 1. X++
     * 2. X--
     * 3. !??
     * 4. end
     */
    int category;
    int operand0;
    int operand1;
    private static final long serialVersionUID = 1L;

    public Instruction(int category) {
        this.category = category;
        this.operand0 = 0;
        this.operand1 = 0;
    }

    public Instruction(int category, int operand0) {
        this.category = category;
        this.operand0 = operand0;
        this.operand1 = 0;
    }

    public Instruction(int category, int operand0, int operand1) {
        this.category = category;
        this.operand0 = operand0;
        this.operand1 = operand1;
    }

    public String toString() {
        String decode = "";
        if (this.category == 0) {
            decode = "x=" + this.operand0;
        } else if (this.category == 1) {
            decode = "x++";
        } else if (this.category == 2) {
            decode = "x--";
        } else if (this.category == 3) {
            decode = "!";
            if (this.operand0 == 0) {
                decode = decode + "A";
            } else if (this.operand0 == 1) {
                decode = decode + "B";
            } else if (this.operand0 == 2) {
                decode = decode + "C";
            }

            decode = decode + this.operand1;
        } else if (this.category == 4) {
            decode = "end";
        }

        if (this.category == -1) {
            decode = "空转";
        }

        return decode;
    }
}
