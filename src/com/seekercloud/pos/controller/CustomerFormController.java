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
        setCustomerID();

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
        if(txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtSalary.getText().isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please fill the all the details to save the customer!").show();
            return;
        }
        Customer customer = new Customer(
                txtID.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Double.parseDouble(txtSalary.getText())
        );
        if (Database.customerTable.add(customer)){
            new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
            setTableData();
            setCustomerID();
            clearFieldData();
        }else {
            new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
        }
    }

    public void clearFieldData(){
        txtName.clear();txtAddress.clear();txtSalary.clear();
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

    private  void setCustomerID(){
        // get last saved customer
        // catch the id (C-001)
        // separate the number from the character
        // increment the separated number
        // concat the character again to the incremented number (C-002)
        // set customer ID

        if(!Database.customerTable.isEmpty()){
            Customer c = Database.customerTable.get(Database.customerTable.size()-1);
            String id = c.getId();
            String dataArray[] = id.split("-");  // ==> ["C","001"]
            id = dataArray[1];     // "001"
            int oldNum = Integer.parseInt(id);   // 1 => 00 remove
            oldNum++;       // 2

            if (oldNum<9){
                txtID.setText("C-00"+oldNum);
            }else if (oldNum<99){
                txtID.setText("C-0"+oldNum);
            }else {
                txtID.setText("C-"+oldNum);
            }
        }else {
            txtID.setText("C-001");
        }
    }
}
