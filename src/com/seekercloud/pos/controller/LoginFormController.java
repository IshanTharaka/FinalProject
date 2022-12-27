package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginFormContext;
    public JFXPasswordField txtPassword;
    public JFXTextField txtEmail;

    public void createAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SignUpForm","Signup Form");
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) loginFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        window.centerOnScreen();
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException, InterruptedException {
        // get user details from interface
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        // check whether details are empty or not
        if(email.isEmpty() || password.isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please fill the all fields!").show();
            return;
        }
        //find the user with inserted username => user table
        for (User u : Database.userTable
             ) {
            if (u.getEmail().equals(email)){
                // check passwords
                // is correct => redirect to the dashboard otherwise the system must show the error.
                if (u.getPassword().equals(password)){
                    Thread.sleep(2000);
                    setUI("DashBoardForm",u.getEmail());
                }else {
                    new Alert(Alert.AlertType.WARNING,"Password is Incorrect!").show();
                }
                return;
            }
        }

        new Alert(Alert.AlertType.INFORMATION,
                "Can't find any user details with this email!").show();
    }
}
