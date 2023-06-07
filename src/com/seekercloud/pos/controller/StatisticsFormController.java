package com.seekercloud.pos.controller;

import com.seekercloud.pos.bo.BoFactory;
import com.seekercloud.pos.bo.BoTypes;
import com.seekercloud.pos.bo.custom.OrderBo;
import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.dto.StatisticsDataDto;
import com.seekercloud.pos.entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StatisticsFormController {

    public AnchorPane statisticsContext;
    public LineChart<String,Double> chart;

    private OrderBo orderBo = BoFactory.getInstance().getBo(BoTypes.ORDER);
    public void initialize() throws SQLException, ClassNotFoundException {
        loadData();
    }

    private void loadData() throws SQLException, ClassNotFoundException {

        ArrayList<StatisticsDataDto> statData  = orderBo.getDateAndIncome();

        if (statData!=null){
            XYChart.Series<String,Double> series = new XYChart.Series<>();

            for (StatisticsDataDto stat:
                    statData) {
                series.getData().add(new XYChart.Data<>(stat.getDate(),stat.getTotalIncome()));
            }
            series.setName("Income Report");
            chart.getData().add(series);
        }else {
            System.out.println("Empty database");
            new Alert(Alert.AlertType.INFORMATION,"No Orders!").show();
        }

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
