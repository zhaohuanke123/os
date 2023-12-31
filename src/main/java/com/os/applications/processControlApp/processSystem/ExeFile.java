package com.os.applications.processControlApp.processSystem;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class ExeFile implements Serializable {
   public int id;
   public Vector<Instruction> instructionArray = new Vector<>();
   public String name;
   public String addName = ".exe";
   public int size;
   private static final long serialVersionUID = 1L;
   public String department = "root";

   public ExeFile(int id) {
      this.id = id;
      this.name = id + "";

      // 从4中指令中随机生成指令
      Random random = new Random();
      int instructionNum = 5 + random.nextInt(11);
      this.size = instructionNum;

      for(int i = 0; i < instructionNum; ++i) {
         int which = random.nextInt(4);
         int num;
         if (which == 0) {
            num = random.nextInt(10);
            this.instructionArray.add(new Instruction(0, num));
         } else if (which == 1) {
            this.instructionArray.add(new Instruction(1));
         } else if (which == 2) {
            this.instructionArray.add(new Instruction(2));
         } else {
            num = random.nextInt(3);
            int num1 = 1 + random.nextInt(5);
            this.instructionArray.add(new Instruction(3, num, num1));
         }
      }

      this.instructionArray.add(new Instruction(4));
   }

   public ExeFile(int id, Vector<Instruction> instructionArray) {
      this.id = id;
      this.instructionArray = instructionArray;
   }

   public int getId() {
      return this.id;
   }

   public Vector<Instruction> getInstructionArray() {
      return this.instructionArray;
   }

   public String toString() {
      StringBuilder s = new StringBuilder();

       for (Instruction instruction : this.instructionArray) {
           s.append(instruction.toString());
           s.append("\n");
       }

      return s.toString();
   }

   public String getName() {
      return this.name + this.addName;
   }
}
