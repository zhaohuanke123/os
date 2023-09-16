package com.os.utils.ui;

import com.os.apps.fileApp.app.MainUI;
import com.os.apps.occupancyApp.OccupancyAppController;
import com.os.datas.InstructionData;
import com.os.datas.ProcessDetailData;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.os.utils.DataLoader;
import com.os.utils.fileSystem.FAT;
import com.os.utils.process.ExecutableFile;
import com.os.utils.process.*;
import com.os.utils.process.Process;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.os.main.MainController;

public class UIThread extends Thread {
    public static Button timeButton1 = null;
    public static Button timeButton2 = null;
    public static ArrayList<ProcessDetailData> processDetailDataArrayList = new ArrayList<>();
    public static TableView<ProcessDetailData> processTable = null;
    public static TableView<InstructionData> nowProcessTable = null;
    public static ArrayList<InstructionData> instructionDataArrayList = new ArrayList<>();
    public static Button nowProcessName = null;
    public static Button nowResult = null;
    public static Button residueSlice = null;
    public Vector<Process> runProcessList;
    public Process runProcess = null;
    public Vector<Process> creatingProcessList;
    public Vector<Process> waitProcessList;
    public Vector<Process> blockProcessList;
    public static Button nowInstruction = null;
    public static CheckBox[] checkBoxes1 = null;
    public static CheckBox[] checkBoxes2 = null;
//    public static VBox[] boxes1 = null;
//    public static HBox[] boxes2 = null;
//    public static Button[] textButtons = null;
    public OccupancyAppController occupancyAppController = null;
    public static Button[] mainButtons = null;
    public static Vector<StageRecord> stageList = null;

    public void init() {
    }

    //region [文件管理器的Update方法]
    public void nowProcessUpdate() {
        if (nowProcessName != null && nowResult != null && nowInstruction != null && residueSlice != null) {
            Platform.runLater(() -> {
                if (UIThread.this.runProcess == null) {
                    UIThread.nowProcessName.setText("");
                    UIThread.nowResult.setText("");
                    UIThread.nowInstruction.setText("");
                    UIThread.residueSlice.setText("");
                } else {
                    UIThread.nowProcessName.setText(UIThread.this.runProcess.name + "");
                    UIThread.nowResult.setText(UIThread.this.runProcess.AX + "");
                    int counter = UIThread.this.runProcess.PC;
                    if (counter >= UIThread.this.runProcess.executableFile.instructionArray.size()) {
                        --counter;
                    }

                    UIThread.nowInstruction.setText(
                            UIThread.this.runProcess.executableFile.getInstructionArray().get(counter) + "");
                    UIThread.residueSlice.setText(ProcessScheduleThread.residueSlice + "");
                }

            });
        }
    }

    public void processTableUpdate() {
        if (processTable != null) {
            Vector<?> updateList = null;
            String selectString = "";
            if (checkBoxes1[0].isSelected()) {
                selectString = checkBoxes1[0].getText();
                updateList = (Vector<?>) ProcessManager.allProcessList.clone();
            } else if (checkBoxes1[1].isSelected()) {
                selectString = checkBoxes1[1].getText();
                updateList = (Vector<?>) ProcessManager.creatingProcessList.clone();
            } else if (checkBoxes1[2].isSelected()) {
                selectString = checkBoxes1[2].getText();
                updateList = (Vector<?>) ProcessManager.waitProcessList.clone();
            } else if (checkBoxes1[3].isSelected()) {
                selectString = checkBoxes1[3].getText();
                updateList = (Vector<?>) ProcessManager.blockProcessList.clone();
            } else if (checkBoxes1[4].isSelected()) {
                selectString = checkBoxes1[4].getText();
                updateList = (Vector<?>) ProcessManager.allProcessList.clone();
            } else if (checkBoxes1[5].isSelected()) {
                selectString = checkBoxes1[5].getText();
                updateList = (Vector<?>) ProcessManager.allProcessList.clone();
            }

            DataLoader.processDetailDataLoad(processDetailDataArrayList, (Vector<Process>) updateList, selectString);
            Platform.runLater(() -> UIThread.processTable.setItems(FXCollections.observableArrayList(UIThread.processDetailDataArrayList)));
            processTable.setRowFactory((row) -> new TableRow<>() {
                public void updateItem(ProcessDetailData item, boolean empty) {
                    super.updateItem(item, empty);
                    String s = "运行态";
                    if (UIThread.checkBoxes2 != null) {
                        for (int i = 0; i < UIThread.checkBoxes2.length; ++i) {
                            if (UIThread.checkBoxes2[i].isSelected()) {
                                if (i == 0) {
                                    s = "新建态";
                                } else if (i == 1) {
                                    s = "就绪态";
                                } else if (i == 2) {
                                    s = "运行态";
                                } else if (i == 3) {
                                    s = "阻塞态";
                                } else if (i == 4) {
                                    s = "已销毁";
                                }
                                break;
                            }
                        }
                    }

                    if (item == null) {
                        this.setId("not-right");
                    } else if (Objects.equals(item.getProcessState(), s)) {
                        this.setId("right");
                    } else {
                        this.setId("not-right");
                    }

                }
            });
        }
    }

    public void nowProcessTableUpdate() {
        if (nowProcessTable != null) {
            if (this.runProcess != null) {
                DataLoader.fileDetailDataLoad(instructionDataArrayList, this.runProcess.executableFile);
                nowProcessTable.setRowFactory((row) -> new TableRow<>() {
                    public void updateItem(InstructionData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            this.setId("not-right");
                        } else if (item.which == UIThread.this.runProcess.PC) {
                            this.setId("right");
                        } else {
                            this.setId("not-right");
                        }

                    }
                });
            } else {
                instructionDataArrayList.clear();
            }

            Platform.runLater(() -> UIThread.nowProcessTable.setItems(FXCollections.observableArrayList(UIThread.instructionDataArrayList)));
        }
    }

    public void timeUpdate() {
        if (timeButton1 != null && timeButton2 != null) {
            Platform.runLater(() -> {
                Date date = new Date();
                UIThread.timeButton1.setText(
                        String.format("%tH", date) + ":" +
                                String.format("%tM", date) + ":" +
                                String.format("%tS", date));
                UIThread.timeButton2.setText("20" + String.format("%ty", date) + "/" +
                        String.format("%tm", date) + "/" +
                        String.format("%td", date));
            });
        }
    }

    //endregion
    public void run() {
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(ProcessManager.slice / ProcessManager.speed);
            } catch (InterruptedException var3) {
                System.out.println(Arrays.toString(var3.getStackTrace()));
            }

//            this.mainButtonsUpdate();
            MainController.getInstance().appButtonUpdate();

            this.runProcessList = (Vector<Process>) ProcessManager.runProcessList.clone();
            this.runProcess = null;
            this.creatingProcessList = (Vector<Process>) ProcessManager.creatingProcessList.clone();
            this.waitProcessList = (Vector<Process>) ProcessManager.waitProcessList.clone();
            this.blockProcessList = (Vector<Process>) ProcessManager.blockProcessList.clone();

            if (!this.runProcessList.isEmpty()) {
                this.runProcess = this.runProcessList.get(0);
            } else {
                Instruction instruction = new Instruction(-1);
                Vector<Instruction> instructionArrayList = new Vector<>();
                instructionArrayList.add(instruction);
                this.runProcess = new Process(-1, new ExecutableFile(-1, instructionArrayList), -1);
                this.runProcess.PC = 0;
            }

            this.timeUpdate();
            this.processTableUpdate();
            this.nowProcessTableUpdate();
            this.nowProcessUpdate();
            if (this.occupancyAppController != null)
                occupancyAppController.occupancyAppUpdate();
//            this.occupancyAppUpdate();
        } while (true);
    }
}
