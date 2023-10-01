package com.os.utils.scene;

import com.os.apps.BaseApp;
import javafx.stage.Stage;

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

    public void addStage(String name, Stage stage) {
        stageList.add(new StageRecord(name, stage));
    }

    public void setAllStageState(boolean isMinimize) {
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
            baseClass = Class.forName("com.os.apps." + stageName + "." + ClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Object o;
        try {
            o = baseClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        BaseApp<?> app = (BaseApp<?>) o;
        appDict.put(stageName, app);
        return app;
    }
}