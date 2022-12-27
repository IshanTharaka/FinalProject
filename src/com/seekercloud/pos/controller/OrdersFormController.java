package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.view.tm.OrdersDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class OrdersFormController {
    public AnchorPane orderFormContext;
    public TableView<OrdersDetailsTM> tblOrders;
    public TableColumn colOrderID;
    public TableColumn colDate;
    public TableColumn colTotalCost;
    public TableColumn colCustomerID;
    public TableColumn colOption;
    public JFXDatePicker dtFrom;
    public JFXDatePicker dtTo;

    public void initialize(){


        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadData();
      //  shortAccordingToDate();
         //=======================
        tblOrders.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                try {
                    openDetailsUI(newValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
         //=======================

    }


    private void openDetailsUI(OrdersDetailsTM value) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/OrderDetailsForm.fxml"));
        Parent parent = fxmlLoader.load();
        OrderDetailsFormController detailsController = fxmlLoader.getController();
        detailsController.loadData(value.getOrderID());
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    private void loadData() {
        String fromDate = dtFrom.getEditor().getText();
        String toDate = dtTo.getEditor().getText();

//        if (){
//
//        }
        try {
            String sql = "SELECT * FROM `Order`";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            ObservableList<OrdersDetailsTM> tmList = FXCollections.observableArrayList();

            while (set.next()){
                Button btn = new Button("Delete");
                OrdersDetailsTM tm = new OrdersDetailsTM(set.getString(1),
                        set.getString(2),
                        set.getDouble(3),
                        set.getString(4),
                        btn);
                tmList.add(tm);

                btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure?",
                        ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> val = alert.showAndWait();
                if (val.get()==ButtonType.YES){
                    try {
                        String sql1 = "DELETE FROM `Order` WHERE orderId=?";
                        PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                        statement1.setString(1,tm.getOrderID());

                        if(statement1.executeUpdate()>0){
                            new Alert(Alert.AlertType.INFORMATION,"Order Deleted!").show();
                            loadData();
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            }
            tblOrders.setItems(tmList);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) orderFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        window.centerOnScreen();
    }


}
