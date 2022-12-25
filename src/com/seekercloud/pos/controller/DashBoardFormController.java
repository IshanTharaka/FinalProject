package com.seekercloud.pos.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DashBoardFormController {
    public Label lblDate;
    public Label lblTime;
    public AnchorPane dashBoardContext;

    public void initialize(){
        setTimeAndDate();
    }

    private void setTimeAndDate() {
        // set date
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String formatedDate = dateFormat.format(date);
//        lblDate.setText(formatedDate);
        lblDate.setText(new SimpleDateFormat("yyyy-MMM-dd").format(new Date()));

        // set time
        final DateFormat format = DateFormat.getDateInstance();
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e->{
//            Calendar cal = Calendar.getInstance();
//            lblTime.setText(format.format(cal.getTime()));
            lblTime.setText(LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm:ss")
            ));
        }),new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void openCustomerFormOnAction(MouseEvent mouseEvent) throws IOException {
        setUI("CustomerForm","Customer");
    }

    public void openPlaceOrderFormOnAction(MouseEvent mouseEvent) {
    }

    public void openStatisticsFormOnAction(MouseEvent mouseEvent) {
    }

    public void openProductsFormOnAction(MouseEvent mouseEvent) throws IOException {
        setUI("ProductForm","Product");
    }

    public void openOrderDetailsFormOnAction(MouseEvent mouseEvent) {
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) dashBoardContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm","Login Form");
    }
}
