package com.os.datas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.os.utils.process.Instruction;

public class InstructionData {
   public int which = -1;
   private StringProperty instruction = new SimpleStringProperty();

   public String getInstruction() {
      return this.instruction.get();
   }

   public StringProperty instructionProperty() {
      return this.instruction;
   }

   public void setInstruction(String instruction) {
      this.instruction.set(instruction);
   }

   public InstructionData(Instruction instruction, int which) {
      this.setInstruction(instruction.toString());
      this.which = which;
   }
}
