package com.os.datas;

import com.os.utils.process.Instruction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InstructionData {
   public int which = -1;
   private final StringProperty instruction = new SimpleStringProperty();

   public void setInstruction(String instruction) {
      this.instruction.set(instruction);
   }

   public InstructionData(Instruction instruction, int which) {
      this.setInstruction(instruction.toString());
      this.which = which;
   }
}
