package com.os;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Test extends Application {

    public static void main(String[] args) {
        // 启动JavaFX应用程序
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        // 设置图表的轴
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("X轴标签");
        yAxis.setLabel("Y轴标签");

        // 创建折线图
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("折线图示例");

        // 创建数据系列
        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("数据系列1");

        // 添加数据点
        dataSeries.getData().add(new XYChart.Data<>(1, 2));
        dataSeries.getData().add(new XYChart.Data<>(2, 4));
        dataSeries.getData().add(new XYChart.Data<>(3, 6));
        dataSeries.getData().add(new XYChart.Data<>(4, 8));
        dataSeries.getData().add(new XYChart.Data<>(5, 10));

        // 将数据系列添加到图表中
        lineChart.getData().add(dataSeries);

        // 创建场景并将图表添加到场景中
        Scene scene = new Scene(lineChart, 800, 600);

        // 设置舞台标题并显示舞台
        stage.setTitle("JavaFX折线图示例");
        stage.setScene(scene);
        stage.show();

        // 添加数据
        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        dataSeries.getData().add(new XYChart.Data<>(6, 12));
                        Thread.sleep(1000);
                        dataSeries.getData().add(new XYChart.Data<>(7, 14));
                        Thread.sleep(1000);
                        dataSeries.getData().add(new XYChart.Data<>(8, 16));
                        Thread.sleep(1000);
                        dataSeries.getData().add(new XYChart.Data<>(9, 18));
                        Thread.sleep(1000);
                        dataSeries.getData().add(new XYChart.Data<>(10, 20));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        ).start();
    }
}
