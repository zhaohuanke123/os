package com.os.utils.scene;

import javafx.stage.Stage;

import java.util.Vector;

import static java.util.Collections.swap;

public class SceneManager {
    private static SceneManager instance = null;
    private final Vector<StageRecord> stageList ;

    private SceneManager() {
        stageList = new Vector<>();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public Stage checkStage(String name) {
        return stageList.stream()
                .filter(stageRecord -> stageRecord.name.equals(name))
                .map(stageRecord -> stageRecord.stage)
                .findFirst().orElse(null);
    }

    // 移除指定窗口
    public void removeStage(String name) {
        stageList.removeIf(stageRecord -> stageRecord.name.equals(name));
    }

    // 更新窗口列表中的信息
    public void updateStageList(String name) {
        for (int i = 0; i < stageList.size(); ++i) {
            // 如果窗口的名称与传入的名称相匹配，更新该窗口的信息
            if (stageList.get(i).name.equals(name)) {
                swap(stageList, i, stageList.size() - 1);
                return;
            }
        }
    }
}
