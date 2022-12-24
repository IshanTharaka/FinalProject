package com.seekercloud.pos.controller;

import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.Customer;
import com.seekercloud.pos.view.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CustomerFormController {
    public AnchorPane customerFormContext;
    public TextField txtID;
    public TextField txtSalary;
    public TextField txtName;
    public TextField txtAddress;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOption;
    public TableView<CustomerTM> tblCustomer;

    public void initialize(){
        setTableData();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }
    public void bachToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        Customer customer = new Customer(
                txtID.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Double.parseDouble(txtSalary.getText())
        );
        if (Database.customerTable.add(customer)){
            new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
            setTableData();
        }else {
            new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
        }
    }

    private void setTableData(){
        ArrayList<Customer> customerList = Database.customerTable;
        ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
        for (Customer c : customerList
             ) {
            Button btn = new Button("Delete");
            CustomerTM tm = new CustomerTM(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
            obList.add(tm);
        }
        tblCustomer.setItems(obList);

    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) customerFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }
}
