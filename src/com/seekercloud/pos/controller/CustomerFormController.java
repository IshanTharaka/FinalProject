package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXButton;
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
import java.util.Optional;

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
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;

    private String searchText="";

    public void initialize(){
        setTableData(searchText);
        setCustomerID();

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        // ===== Listener for table========
        tblCustomer.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            if (null!=newValue){
                setCustomerData(newValue);
            }
        });

        // ===== Listener for search========
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            setTableData(searchText);
        });
    }

    private  void setCustomerData(CustomerTM tm){
        txtID.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));

        btnSaveUpdate.setText("Update Customer");
    }
    public void bachToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        if(txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtSalary.getText().isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please fill the all the details to save the customer!").show();
            return;
        }

        if (!isNumeric(txtSalary.getText())){
            new Alert(Alert.AlertType.WARNING,"Salary must be a number!").show();
            txtSalary.requestFocus();
            return;
        }

        Customer customer = new Customer(
                txtID.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Double.parseDouble(txtSalary.getText())
        );

        if(btnSaveUpdate.getText().equalsIgnoreCase("Save Customer")){
            // save
            if (Database.customerTable.add(customer)){
                new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
                setTableData(searchText);
                setCustomerID();
                clear();
            }else {
                new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
            }
        }else {
            for (Customer c: Database.customerTable
            ) {
                if (txtID.getText().equalsIgnoreCase(c.getId())){
                    c.setName(txtName.getText());
                    c.setAddress(txtAddress.getText());
                    c.setSalary(Double.parseDouble(txtSalary.getText()));
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Details Updated!").show();
                    setTableData(searchText);
                    clear();
                }
            }
        }
        txtName.requestFocus();
    }

    private boolean isNumeric(String string){
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    public void clear(){
        btnSaveUpdate.setText("Save Customer");
        clearFieldData();
        setCustomerID();
    }

    public void clearFieldData(){
        txtName.clear();txtAddress.clear();txtSalary.clear();
    }

    private void setTableData(String text){
        text = text.toLowerCase();
        ArrayList<Customer> customerList = Database.customerTable;
        ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
        for (Customer c : customerList
             ) {
            if(c.getName().toLowerCase().contains(text) || c.getAddress().toLowerCase().contains(text)){
                Button btn = new Button("Delete");
                CustomerTM tm = new CustomerTM(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
                obList.add(tm);

                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?",
                            ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        Database.customerTable.remove(c);
                        new Alert(Alert.AlertType.INFORMATION,"Customer Deleted!").show();
                        setTableData(searchText);
                        clear();
                    }
                });
            }
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

    public void clearDataOnAction(ActionEvent actionEvent) {
        clear();
        txtName.requestFocus();
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        clear();
        txtName.requestFocus();
    }
}
