package com.seekercloud.pos.controller;

import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerFormController {
    public AnchorPane customerFormContext;
    public TextField txtID;
    public TextField txtSalary;
    public TextField txtName;
    public TextField txtAddress;

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
        }else {
            new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
        }
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) customerFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }
}
