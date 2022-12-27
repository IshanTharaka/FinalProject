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

public class SignUpFormController {
    public JFXTextField txtFullName;
    public JFXPasswordField txtPassword;
    public JFXTextField txtContact;
    public JFXTextField txtEmail;
    public AnchorPane signupFormContext;
    public JFXPasswordField txtRePassword;

    public void signUpOnAction(ActionEvent actionEvent) throws InterruptedException, IOException {
        // check whether details are empty or not
        if(txtFullName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtContact.getText().isEmpty() || txtPassword.getText().isEmpty() || txtRePassword.getText().isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please fill the all fields!").show();
            return;
        }
        // Check the password is matched?
        String realPwd = txtPassword.getText().trim();  // __NDF_ After trim() ==> NDF
        String matchPwd = txtRePassword.getText().trim();

        if(!realPwd.equals(matchPwd)){
            new Alert(Alert.AlertType.WARNING,"Both passwords should be matched !").show();
            return;  // we will stop the JVM
        } else if (realPwd.isEmpty() || matchPwd.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,"Passwords Cannot be empty !").show();
            return;
        }

        User user = new User(
            txtEmail.getText().trim(),txtFullName.getText().trim(),txtContact.getText().trim(),realPwd
        );

        if(saveUser(user)){
            new Alert(Alert.AlertType.CONFIRMATION,"User Registered.").show();
            clearFields();
            Thread.sleep(2000);  // Wait 2 seconds
            setUI("DashBoardForm",user.getEmail());
        }else {
            new Alert(Alert.AlertType.WARNING,"Already Exist, Try Again !").show();
        }
    }

    private void clearFields(){
        txtEmail.clear();txtContact.clear();txtPassword.clear();
        txtFullName.clear();txtRePassword.clear();
    }

    private boolean saveUser(User u){
        for (User tempUser : Database.userTable
             ) {
            if(tempUser.getEmail().equals(u.getEmail())){
                return false;
            }
        }
        return Database.userTable.add(u);   // inbuilt class ==> java.util
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) signupFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm","Login Form");
    }
}
