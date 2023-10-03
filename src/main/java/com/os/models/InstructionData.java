package com.os.models;

import com.os.applications.processApp.processSystem.Instruction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InstructionData {
   public int which = -1;
   private final StringProperty instruction = new SimpleStringProperty();

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
