package com.os.apps.fileApp.Controller;

import com.os.apps.fileApp.FileApp;
import com.os.apps.fileApp.app.TipWindow;
import com.os.utils.fileSystem.Disk;
import com.os.utils.fileSystem.Folder;
import com.os.utils.fileSystem.Path;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;

public class DelViewCtl extends BaseFileCtl {
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text text;
    private Disk block;

    public void init(final Stage stage, final FileApp mainView, String tipString, final Disk block) {
        super.init(stage);
        this.text.setText(tipString);
        this.block = block;

        this.acceptButton.setOnMouseClicked(event -> {
            stage.close();
            Path thisPath = null;
            if (block.getObject() instanceof Folder) {
                thisPath = ((Folder) block.getObject()).getPath();
            }

            int res = FileApp.fat.delete(block);
            if (res == 0) {
                mainView.removeNode(mainView.getRecentNode(), thisPath);

//                try {
//                    DelViewCtl.this.tipOpen("删除文件夹成功");
//                } catch (Exception var8) {
//                    System.out.println(Arrays.toString(var8.getStackTrace()));
//                }
            } else if (res == 1) {
//                try {
//                    DelViewCtl.this.tipOpen("删除文件成功");
//                } catch (Exception var7) {
//                    System.out.println(Arrays.toString(var7.getStackTrace()));
//                }
            } else if (res == 2) {
                try {
                    DelViewCtl.this.tipOpen("文件夹不为空");
                } catch (Exception var6) {
                    System.out.println(Arrays.toString(var6.getStackTrace()));
                }
            } else {
                try {
                    DelViewCtl.this.tipOpen("文件未关闭");
                } catch (Exception var5) {
                    System.out.println(Arrays.toString(var5.getStackTrace()));
                }
            }

            mainView.controller.flowPane.getChildren().removeAll(mainView.controller.flowPane.getChildren());

            mainView.addIcon(FileApp.fat.getBlockList(mainView.recentPath), mainView.recentPath);
        });
        this.cancelButton.setOnMouseClicked(event -> stage.close());
    }

    public void tipOpen(String tipString) throws Exception {
        Stage stage = new Stage();
        TipWindow tipWindow = new TipWindow(tipString);
        tipWindow.start(stage);
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
    }
}
