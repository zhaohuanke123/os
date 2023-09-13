package com.os.utils.ui;

import com.os.apps.fileApp.app.MainUI;
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
    public static VBox[] boxes1 = null;
    public static HBox[] boxes2 = null;
    public static Button[] textButtons = null;
    public static Button[] mainButtons = null;
    public static Vector<StageRecord> stageList = null;

    public void init() {
    }

    public void mainButtonsUpdate() {
        if (mainButtons != null && stageList != null) {
            Stage stage;

            stage = MainController.checkStage("com/os/apps/systemFileApp");
            if (stage != null && !stage.isShowing()) {
                mainButtons[0].setStyle("");
            }

            stage = MainController.checkStage("com/os/apps/fileApp");
            if (stage != null && !stage.isShowing()) {
                mainButtons[1].setStyle("");
            }

            stage = MainController.checkStage("com/os/apps/processApp");
            if (stage != null && !stage.isShowing()) {
                mainButtons[2].setStyle("");
            }

            stage = MainController.checkStage("com/os/apps/occupancyApp");
            if (stage != null && !stage.isShowing()) {
                mainButtons[3].setStyle("");
            }

            stage = MainController.checkStage("com/os/apps/helpApp");
            if (stage != null && !stage.isShowing()) {
                mainButtons[4].setStyle("");
            }

        }
    }

    //region [占用管理器的Update方法]
    public void occupancyAppUpdate() {
        this.occupancyTextUpdate();
        this.occupancyBoxes1Update();
        this.occupancyBoxes2Update();
    }

    public void occupancyTextUpdate() {
        if (textButtons != null) {
            Platform.runLater(() -> {
                int num = 0;
                int allx = OccupancyManager.allMemory.length;

                for (int ix = 0; ix < allx; ++ix) {
                    if (OccupancyManager.allMemory[ix] == 1) {
                        ++num;
                    }
                }

                double percent = (double) num / (double) allx * 100.0;
                String result = String.format("%.2f", percent);
                UIThread.textButtons[0].setText(num + "B/" + allx + "B(" + result + "%)");
                int i;
                if (MainUI.fat != null) {
                    num = 0;
                    int all = 256;

                    for (i = 0; i < 256; ++i) {
                        if (MainUI.fat.getDiskBlocks()[i].getIndex() != 0) {
                            ++num;
                        }
                    }

                    percent = (double) num / (double) all * 100.0;
                    result = String.format("%.2f", percent);
                    UIThread.textButtons[1].setText(num + "/" + all + "(" + result + "%)");
                }

                num = OccupancyManager.getBusyDeviceNum();
                allx = OccupancyManager.All_DEVICE_SIZE;
                percent = (double) num / (double) allx * 100.0;

                result = String.format("%.2f", percent);
                UIThread.textButtons[2].setText(num + "/" + allx + "(" + result + "%)");
                num = 10 - OccupancyManager.freePcbList.size();
                int all_xx = OccupancyManager.PCB_SIZE;
                percent = (double) num / (double) all_xx * 100.0;
                result = String.format("%.2f", percent);
                UIThread.textButtons[3].setText(num + "/" + all_xx + "(" + result + "%)");
            });
        }
    }

    public void occupancyBoxes1Update() {
        if (boxes1 != null) {
            int num = 0;
            int all = OccupancyManager.allMemory.length;

            for (int i = 0; i < all; ++i) {
                if (OccupancyManager.allMemory[i] == 1) {
                    ++num;
                }
            }

            double height = boxes1[0].getHeight() - 2.0;
            double width = boxes1[0].getWidth() - 2.0;
            Region region = (Region) boxes1[0].getChildren().get(0);
            double percent = (double) num / (double) all;
            region.setPrefSize(width, percent * height);
            region.setMinSize(width, percent * height);
            region.setMaxSize(width, percent * height);
            int i;
            if (MainUI.fat != null) {
                num = 0;

                for (i = 0; i < 256; ++i) {
                    if (MainUI.fat.getDiskBlocks()[i].getIndex() != 0) {
                        ++num;
                    }
                }

                System.out.println("usedNum:" + num);
                height = boxes1[1].getHeight() - 2.0;
                width = boxes1[1].getWidth() - 2.0;
                region = (Region) boxes1[1].getChildren().get(0);
                percent = (double) num / 256.0;
                region.setPrefSize(width, percent * height);
                region.setMinSize(width, percent * height);
                region.setMaxSize(width, percent * height);
            }

            num = OccupancyManager.getBusyDeviceNum();
            all = OccupancyManager.All_DEVICE_SIZE;
            percent = (double) num / (double) all;

            height = boxes1[2].getHeight() - 2.0;
            width = boxes1[2].getWidth() - 2.0;
            region = (Region) boxes1[2].getChildren().get(0);
            region.setPrefSize(width, percent * height);
            region.setMinSize(width, percent * height);
            region.setMaxSize(width, percent * height);
            num = OccupancyManager.PCB_SIZE - OccupancyManager.freePcbList.size();

            all = OccupancyManager.PCB_SIZE;
            percent = (double) num / (double) all;
            height = boxes1[3].getHeight() - 2.0;
            width = boxes1[3].getWidth() - 2.0;
            region = (Region) boxes1[3].getChildren().get(0);
            region.setPrefSize(width, percent * height);
            region.setMinSize(width, percent * height);
            region.setMaxSize(width, percent * height);
        }
    }

    public void occupancyBoxes2Update() {
        if (boxes2 != null) {
            Platform.runLater(() -> {

                for (int memory_index = 0; memory_index < OccupancyManager.MEMORY_SIZE; ++memory_index) {
                    if (OccupancyManager.allMemory[memory_index] == 0) {
                        UIThread.boxes2[0].getChildren().get(memory_index).setId("emptyBox");
                    } else {
                        UIThread.boxes2[0].getChildren().get(memory_index).setId("memoryInBox1");
                    }
                }

                if (MainUI.fat != null) {
                    for (int disk_index = 0; disk_index < FAT.DISK_NUM; ++disk_index) {
                        if (MainUI.fat.getDiskBlocks()[disk_index].getIndex() != 0) {
                            UIThread.boxes2[1].getChildren().get(disk_index).setId("diskInBox1");
                        } else {
                            UIThread.boxes2[1].getChildren().get(disk_index).setId("emptyBox");
                        }
                    }
                }

                for (int a_device_index = 0; a_device_index < OccupancyManager.A_DEVICE_SIZE; ++a_device_index) {
                    if (OccupancyManager.aDevice[a_device_index] == 0) {
                        UIThread.boxes2[2].getChildren().get(a_device_index).setId("emptyBox");
                    } else {
                        UIThread.boxes2[2].getChildren().get(a_device_index).setId("deviceInBox1");
                    }
                }

                for (int b_device_index = 0; b_device_index < OccupancyManager.B_DEVICE_SIZE; ++b_device_index) {
                    if (OccupancyManager.bDevice[b_device_index] == 0) {
                        UIThread.boxes2[2].getChildren().get(2 + b_device_index).setId("emptyBox");
                    } else {
                        UIThread.boxes2[2].getChildren().get(2 + b_device_index).setId("deviceInBox1");
                    }
                }

                for (int c_device_index = 0; c_device_index < OccupancyManager.C_DEVICE_SIZE; ++c_device_index) {
                    if (OccupancyManager.cDevice[c_device_index] == 0) {
                        UIThread.boxes2[2].getChildren().get(5 + c_device_index).setId("emptyBox");
                    } else {
                        UIThread.boxes2[2].getChildren().get(5 + c_device_index).setId("deviceInBox1");
                    }
                }

                int i, pcbId;
                for (i = 0; i < OccupancyManager.PCB_SIZE; ++i) {
                    UIThread.boxes2[3].getChildren().get(i).setId("emptyBox");
                }

                for (i = 0; i < UIThread.this.creatingProcessList.size(); ++i) {
                    pcbId = UIThread.this.creatingProcessList.get(i).pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                        UIThread.boxes2[3].getChildren().get(pcbId).setId("pcbInBox0");
                    }
                }

                for (i = 0; i < UIThread.this.waitProcessList.size(); ++i) {
                    pcbId = UIThread.this.waitProcessList.get(i).pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                        UIThread.boxes2[3].getChildren().get(pcbId).setId("pcbInBox1");
                    }
                }

                if (UIThread.this.runProcess != null) {
                    pcbId = UIThread.this.runProcess.pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                        UIThread.boxes2[3].getChildren().get(pcbId).setId("pcbInBox2");
                    }
                }

                for (i = 0; i < UIThread.this.blockProcessList.size(); ++i) {
                    pcbId = UIThread.this.blockProcessList.get(i).pcbID;
                    if (OccupancyManager.checkPCBIndex(pcbId)) {
                        UIThread.boxes2[3].getChildren().get(pcbId).setId("pcbInBox3");
                    }
                }

            });
        }
    }
    //endregion

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
            Date date = new Date();
            final String hour = String.format("%tH", date);
            final String minute = String.format("%tM", date);
            String second = String.format("%tS", date);
            final String year = String.format("%ty", date);
            final String month = String.format("%tm", date);
            final String day = String.format("%td", date);
            Platform.runLater(() -> {
                UIThread.timeButton1.setText(hour + ":" + minute + ":" + second);
                UIThread.timeButton2.setText("20" + year + "/" + month + "/" + day);
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

            this.mainButtonsUpdate();
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
            this.occupancyAppUpdate();
        } while (true);
    }
}
