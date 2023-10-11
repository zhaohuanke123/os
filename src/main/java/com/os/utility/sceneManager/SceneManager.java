package com.os.utility.sceneManager;

import com.os.applications.BaseApp;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;
import java.util.Vector;

import static java.util.Collections.swap;

public class SceneManager {
    private static SceneManager instance = null;
    private final Vector<StageRecord> stageList;
    private final TreeMap<String, BaseApp<?>> appDict;

    private SceneManager() {
        stageList = new Vector<>();
        appDict = new TreeMap<>();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void AppStart(String stageName) {
        // 检查窗口是否已存在
        Stage stage = checkStage(stageName);

        // 如果窗口存在但未显示，将其移除
        if (stage != null && !stage.isShowing())
            removeStage(stageName);

        // 再次检查窗口是否存在
        stage = checkStage(stageName);

        // 如果窗口不存在，则创建新的窗口并添加到 stageList 中
        if (stage == null) {
            try {
                stage = new Stage();
                // 使用 SystemFileApp 实例初始化新窗口
                getApp(stageName).start(stage);
                // 将新窗口记录添加到窗口列表
                addStage(stageName, stage);
            } catch (IOException e) {
                e.getStackTrace();
            }
        } else if (!stage.isIconified()) {
            stage.setIconified(true);
            return;
        }

        // 如果窗口已显示，将其显示在最前面
        if (stage.isShowing()) stage.show();

        updateStageList(stageName);

        // 设置窗口始终位于其他窗口之上
        stage.setAlwaysOnTop(true);
        // 将窗口从最小化状态还原
        stage.setIconified(false);
        // 将窗口置于最前
        stage.toFront();
    }

    private Stage checkStage(String name) {
        return stageList.stream()
                .filter(stageRecord -> stageRecord.name.equals(name))
                .map(stageRecord -> stageRecord.stage)
                .findFirst().orElse(null);
    }

    // 移除指定窗口
    private void removeStage(String name) {
        stageList.removeIf(stageRecord -> stageRecord.name.equals(name));
    }

    // 更新窗口列表中的信息
    private void updateStageList(String name) {
        for (int i = 0; i < stageList.size(); ++i) {
            // 如果窗口的名称与传入的名称相匹配，更新该窗口的信息
            if (stageList.get(i).name.equals(name)) {
                swap(stageList, i, stageList.size() - 1);
                return;
            }
        }
    }

    private void addStage(String name, Stage stage) {
        stageList.add(new StageRecord(name, stage));
    }

    public void setAllStageHideOrShow(boolean isMinimize) {
        stageList.forEach(stageRecord -> {
            Stage stage = stageRecord.stage;
            if (stage != null && stage.isShowing()) {
                stage.setIconified(isMinimize);
            }
        });
    }

    public BaseApp<?> getApp(String stageName) {
        if (appDict.containsKey(stageName)) {
            return appDict.get(stageName);
        }
        // Java反射机制创建类
        Class<?> baseClass;
        String ClassName = stageName;
        // 首字母大写
        ClassName = ClassName.substring(0, 1).toUpperCase() + ClassName.substring(1);
        try {
            baseClass = Class.forName(stageName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Object o;
        try {
            o = baseClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        BaseApp<?> app = (BaseApp<?>) o;
        appDict.put(stageName, app);
        return app;
    }

    public boolean isStageClosed(String stageName) {
        Stage stage = checkStage(stageName);
        return stage != null && !stage.isShowing();
    }
}
