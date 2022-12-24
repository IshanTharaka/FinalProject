package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class SignUpFormController {
    public JFXTextField txtFullName;
    public JFXPasswordField txtPassword;
    public JFXTextField txtContact;
    public JFXTextField txtEmail;
    public JFXTextField txtRePassowrd;

    public void signUpOnAction(ActionEvent actionEvent) {
        // Check the password is matched?
        String realPwd = txtPassword.getText().trim();  // __NDF_ After trim() ==> NDF
        String matchPwd = txtRePassowrd.getText().trim();

        if(!realPwd.equals(matchPwd)){
            new Alert(Alert.AlertType.WARNING,"Both passwords should be matched !").show();
            return;  // we will stop the JVM
        }

        User user = new User(
            txtEmail.getText().trim(),txtFullName.getText().trim(),txtContact.getText().trim(),realPwd
        );

        if(saveUser(user)){
            new Alert(Alert.AlertType.CONFIRMATION,"User Registered.").show();
            clearFields();
        }else {
            new Alert(Alert.AlertType.WARNING,"Try Again !").show();
        }
    }

    private void clearFields(){
        txtEmail.clear();txtContact.clear();txtPassword.clear();
        txtFullName.clear();txtRePassowrd.clear();
    }

    private boolean saveUser(User u){
        return Database.userTable.add(u);   // inbuilt class ==> java.util
    }
}
