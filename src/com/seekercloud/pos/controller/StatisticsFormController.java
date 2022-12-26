package com.seekercloud.pos.controller;

import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class StatisticsFormController {

    public AnchorPane statisticsContext;
    public LineChart<String,Double> chart;

    public void initialize(){
        loadData();
    }

    private void loadData() {

        if (!Database.orderTable.isEmpty()){
            XYChart.Series<String,Double> series = new XYChart.Series<>();
            ObservableList<XYChart.Series<String,Double>> obList = FXCollections.observableArrayList();

            for (Order temp:Database.orderTable
                 ) {
                series.getData().add(new XYChart.Data<>(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .format(temp.getPlaceDate()),
                        temp.getTotal()
                ));
            }
            obList.add(series);
            chart.setData(obList);
        }


//        XYChart.Series<String,Number> series = new XYChart.Series<>();
//        series.getData().add(new XYChart.Data<>("2022-12-25",25000));
//        series.getData().add(new XYChart.Data<>("2022-12-26",89550));
//        series.getData().add(new XYChart.Data<>("2022-12-27",75000));
//        series.getData().add(new XYChart.Data<>("2022-12-28",2500));
//        series.getData().add(new XYChart.Data<>("2022-12-24",65000));
//        series.setName("Income Report");
//        chart.getData().add(series);

    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) statisticsContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }
}
