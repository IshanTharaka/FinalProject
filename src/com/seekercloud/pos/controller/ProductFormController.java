package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.Customer;
import com.seekercloud.pos.model.Product;
import com.seekercloud.pos.view.tm.CustomerTM;
import com.seekercloud.pos.view.tm.ProductTM;
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

public class ProductFormController {
    public AnchorPane productFormContext;
    public TextField txtCode;
    public TextField txtQtyOnHand;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;
    public TableView<ProductTM> tblProduct;
    public TableColumn colCode;
    public TableColumn colUnitPrice;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colQR;
    public TableColumn colOption;

    private String searchText="";
    public void initialize(){
        setTableData(searchText);
        setProductCode();

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colQR.setCellValueFactory(new PropertyValueFactory<>("btn"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        // ===== Listener for table========
        tblProduct.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null!=newValue){
                        setProductData(newValue);
                    }
                });

        // ===== Listener for search========
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            setTableData(searchText);
        });
    }

    private  void setProductData(ProductTM tm){
        txtCode.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));

        btnSaveUpdate.setText("Update Product");
    }

    private void setTableData(String text){
        text = text.toLowerCase();
        ArrayList<Product> productList = Database.productTable;
        ObservableList<ProductTM> obList = FXCollections.observableArrayList();
        for (Product p : productList
        ) {
            if(p.getDescription().toLowerCase().contains(text)){
                Button btn = new Button("Delete");
                ProductTM tm = new ProductTM(p.getCode(),p.getDescription(),p.getUnitPrice(),p.getQtyOnHand(),btn);
                obList.add(tm);

                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?",
                            ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        Database.productTable.remove(p);
                        new Alert(Alert.AlertType.INFORMATION,"Product Deleted!").show();
                        setTableData(searchText);
                        clear();
                    }
                });
            }
        }
        tblProduct.setItems(obList);

    }
    public void clear(){
        btnSaveUpdate.setText("Save Product");
        clearFieldData();
        setProductCode();
    }

    public void clearFieldData(){
        txtDescription.clear();txtUnitPrice.clear();txtQtyOnHand.clear();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }
    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) productFormContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }

    public void newProductOnAction(ActionEvent actionEvent) {
        clear();
        txtDescription.requestFocus();
    }

    public void saveProductOnAction(ActionEvent actionEvent) {
        if(txtDescription.getText().isEmpty() || txtQtyOnHand.getText().isEmpty() || txtUnitPrice.getText().isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please fill the all the details to save the product!").show();
            return;
        }

        if (!isNumeric(txtUnitPrice.getText())){
            new Alert(Alert.AlertType.WARNING,"Unit price must be a number!").show();
            txtUnitPrice.requestFocus();
            return;
        } else if (!isNumeric(txtQtyOnHand.getText())) {
            new Alert(Alert.AlertType.WARNING,"QTY on hand must be a number!").show();
            txtQtyOnHand.requestFocus();
            return;
        }

        Product product = new Product(
                txtCode.getText(),
                txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText())
        );

        if(btnSaveUpdate.getText().equalsIgnoreCase("Save Product")){
            // save
            if (Database.productTable.add(product)){
                new Alert(Alert.AlertType.INFORMATION,"Product Saved!").show();
                setTableData(searchText);
                setProductCode();
                clear();
            }else {
                new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
            }
        }else {
            for (Product p: Database.productTable
            ) {
                if (txtCode.getText().equalsIgnoreCase(p.getCode())){
                    p.setDescription(txtDescription.getText());
                    p.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                    p.setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                    new Alert(Alert.AlertType.CONFIRMATION,"Product Details Updated!").show();
                    setTableData(searchText);
                    clear();
                }
            }
        }
        txtDescription.requestFocus();
    }

    public void clearDataOnAction(ActionEvent actionEvent) {
        clear();
        txtDescription.requestFocus();
    }

    private  void setProductCode(){
        if(!Database.productTable.isEmpty()){
            Product p = Database.productTable.get(Database.productTable.size()-1);
            String id = p.getCode();
            String dataArray[] = id.split("-");
            id = dataArray[1];
            int oldNum = Integer.parseInt(id);
            oldNum++;

            if (oldNum<9){
                txtCode.setText("P-00"+oldNum);
            }else if (oldNum<99){
                txtCode.setText("P-0"+oldNum);
            }else {
                txtCode.setText("P-"+oldNum);
            }
        }else {
            txtCode.setText("P-001");
        }
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
}
