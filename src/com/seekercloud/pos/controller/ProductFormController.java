package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekercloud.pos.bo.BoFactory;
import com.seekercloud.pos.bo.BoTypes;
import com.seekercloud.pos.bo.custom.ProductBo;
import com.seekercloud.pos.dao.DaoFactory;
import com.seekercloud.pos.dao.DaoTypes;
import com.seekercloud.pos.dao.custom.ProductDao;
import com.seekercloud.pos.dao.custom.impl.ProductDaoImpl;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.dto.ProductDto;
import com.seekercloud.pos.entity.Product;
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
import java.sql.*;
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

    private ProductBo productBo = BoFactory.getInstance().getBo(BoTypes.PRODUCT);

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
        searchText = "%"+text+"%";
        try {
            ObservableList<ProductTM> obList = FXCollections.observableArrayList();

            ArrayList<ProductDto> productList = productBo.searchProducts(searchText);
            for (ProductDto p : productList){
                Button btn = new Button("Delete");
                ProductTM tm = new ProductTM(
                        p.getCode(),
                        p.getDescription(),
                        p.getUnitPrice(),
                        p.getQtyOnHand(),
                        btn);
                obList.add(tm);

                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure?",
                            ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> val = alert.showAndWait();
                    if (val.get()==ButtonType.YES){
                        try {
                            if(productBo.deleteProduct(tm.getCode())){
                                new Alert(Alert.AlertType.INFORMATION,"Product Deleted!").show();
                                setTableData(searchText);
                                clear();
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            tblProduct.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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
        window.centerOnScreen();
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

        if(btnSaveUpdate.getText().equalsIgnoreCase("Save Product")){
            // save
            try {
                boolean isProductSaved = productBo.saveProduct(
                        new ProductDto(
                        txtCode.getText(),
                        txtDescription.getText(),
                        Double.parseDouble(txtUnitPrice.getText()),
                        Integer.parseInt(txtQtyOnHand.getText()))
                );

                if (isProductSaved){
                    new Alert(Alert.AlertType.INFORMATION,"Product Saved!").show();
                    setTableData(searchText);
                    setProductCode();
                    clear();
                }else {
                    new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                boolean isProductUpdated = productBo.updateProduct(
                        new ProductDto(
                                txtCode.getText(),
                                txtDescription.getText(),
                                Double.parseDouble(txtUnitPrice.getText()),
                                Integer.parseInt(txtQtyOnHand.getText()))
                );

                if (isProductUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"Product Details Updated!").show();
                    setTableData(searchText);
                    setProductCode();
                    clear();
                }else {
                    new Alert(Alert.AlertType.CONFIRMATION,"Try Again!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        txtDescription.requestFocus();
    }

    public void clearDataOnAction(ActionEvent actionEvent) {
        clear();
        txtDescription.requestFocus();
    }

    private  void setProductCode(){

        try{
            String sql1 = "SELECT * FROM Product ORDER BY code DESC LIMIT 1";
            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
            ResultSet set = statement1.executeQuery();

            if (set.next()) {
                String lastPrimaryKey = set.getString(1);

                String dataArray[] = lastPrimaryKey.split("-");  // ==> ["C","001"]
                lastPrimaryKey = dataArray[1];     // "001"
                int oldNum = Integer.parseInt(lastPrimaryKey);   // 1 => 00 remove
                oldNum++;       // 2

                if (oldNum<=9){
                    txtCode.setText("P-00"+oldNum);
                }else if (oldNum<=99){
                    txtCode.setText("P-0"+oldNum);
                }else {
                    txtCode.setText("P-"+oldNum);
                }
            }else {
                txtCode.setText("P-001");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
