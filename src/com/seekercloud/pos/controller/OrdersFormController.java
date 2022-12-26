package com.seekercloud.pos.controller;

import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.Order;
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
import java.text.SimpleDateFormat;
import java.util.Optional;

public class OrdersFormController {
    public AnchorPane orderFormContext;
    public TableView<OrdersDetailsTM> tblOrders;
    public TableColumn colOrderID;
    public TableColumn colDate;
    public TableColumn colTotalCost;
    public TableColumn colCustomerID;
    public TableColumn colOption;

    public void initialize(){
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadData();

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
        ObservableList<OrdersDetailsTM> tmList = FXCollections.observableArrayList();
        for (Order o : Database.orderTable
             ) {
            Button btn = new Button("Delete");
            OrdersDetailsTM tm = new OrdersDetailsTM(
                    o.getOrderID(),
                    new SimpleDateFormat("yyyy-MM-dd").format(o.getPlaceDate()),
                    o.getTotal(),o.getCustomer(),btn);
            tmList.add(tm);

            btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure?",
                        ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> val = alert.showAndWait();
                if (val.get()==ButtonType.YES){
                    Database.orderTable.remove(o);
                    new Alert(Alert.AlertType.INFORMATION,"Order Deleted!").show();
                    loadData();
                }
            });
        }
        tblOrders.setItems(tmList);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) orderFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }

}
