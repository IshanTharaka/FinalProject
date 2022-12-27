package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.CartItem;
import com.seekercloud.pos.model.Customer;
import com.seekercloud.pos.model.Order;
import com.seekercloud.pos.model.Product;
import com.seekercloud.pos.view.tm.CartTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public class PlaceOrderFormController {
    public AnchorPane placeOrderContext;
    public ComboBox<String> cmdCustomerID;
    public ComboBox<String> cmbProductsCode;
    public TextField txtName;
    public TextField txtSalary;
    public TextField txtAddress;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtQty;
    public TextField txtOrderID;
    public TextField txtOrderCount;
    public TextField txtOrderDate;
    public TableView<CartTM> tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colOption;
    public TableColumn colQty;
    public TableColumn colTotal;
    public JFXButton addToCartBtn;
    public Label lblTotalCost;

    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadCustomerIDs();
        loadProductCodes();
        setDate();
        generateOrderID();

        //=========== LListener =============
        cmdCustomerID.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setCustomerData(newValue);
                });
        //=========== LListener =============
        //=========== LListener =============
        cmbProductsCode.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setProductData(newValue);
                });
        //=========== LListener =============
    }

    private void setDate() {
        txtOrderDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void setProductData(String code) {
        txtQty.requestFocus();
        Product product = Database.productTable.stream().filter(e -> e.getCode().equals(code))
                .findFirst().orElse(null);  // NO need to use for each loop here. Use this method

        if (product!=null){
            txtDescription.setText(product.getDescription());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(product.getQtyOnHand()));
            
        }
    }

    private void setCustomerData(String id) {
        Stream<Customer> customerList = Database.customerTable.stream().filter(e -> e.getId().equals(id));
        Optional<Customer> first = customerList.findFirst();
        if (first.isPresent()){
            Customer customer = first.get();
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtSalary.setText(String.valueOf(customer.getSalary()));
        }
    }

    private void loadCustomerIDs() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        for (Customer c: Database.customerTable
             ) {
            obList.add(c.getId());
        }
        cmdCustomerID.setItems(obList);
    }

    private void loadProductCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        for (Product p: Database.productTable
        ) {
            obList.add(p.getCode());
        }
        cmbProductsCode.setItems(obList);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) placeOrderContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));

    }

    ObservableList<CartTM> tmList = FXCollections.observableArrayList();
    public void addToCart(ActionEvent actionEvent) {
        if (!checkCustomerAndProduct()){
            return;
        }

        if (!isNumeric(txtQty.getText())){
            new Alert(Alert.AlertType.WARNING,"Quantity must be a number!").show();
            txtQty.requestFocus();
            return;
        }

        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double total = qty*unitPrice;

        if (!checkQTY(qty)){
            new Alert(Alert.AlertType.WARNING,"Invalid Quantity!").show();
            return;
        }

        Button btn = new Button("Remove");

        CartTM existTM = isExist(cmbProductsCode.getValue());
        CartTM tm = new CartTM(cmbProductsCode.getValue()
                ,txtDescription.getText(),unitPrice,qty,total,btn);

        if (existTM!=null){
            if (!checkQTY(existTM.getQty()+qty)){
                new Alert(Alert.AlertType.WARNING,"Invalid Quantity!").show();
                return;
            }
            // update
            existTM.setQty(existTM.getQty()+qty);
            existTM.setTotal(existTM.getTotal()+total);
        }else {
            tmList.add(tm);
        }

        btn.setOnAction(event -> {
            tmList.remove(tm);
            tblCart.refresh();
            setTotalAndCount();
        });

        tblCart.setItems(tmList);
        tblCart.refresh();
        setTotalAndCount();
        clearFields();
    }

    private boolean isNumeric(String string){
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }
    private boolean checkCustomerAndProduct() {
        if (txtName.getText().isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please select the customer or otherwise add a new customer!").show();
            return false;
        }else if (txtDescription.getText().isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please select the products with required quantities!").show();
            return false;
        }
        return true;
    }

    private boolean checkQTY(int qty){
//        if (Integer.parseInt(txtQtyOnHand.getText())<qty){
//            new Alert(Alert.AlertType.WARNING,"Invalid Quantity!").show();
//            return false;  // can't add
//        }
//        return true; // add more

        return Integer.parseInt(txtQtyOnHand.getText()) >= qty;
    }
    private void clearFields() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        cmbProductsCode.requestFocus();
    }

    private CartTM isExist(String id){
        return tmList.stream().filter(e -> e.getCode().equals(id)).findFirst().orElse(null);
    }

    private void setTotalAndCount(){
//        txtOrderCost.setText(String.valueOf(0));
//         tmList.forEach(e -> {
//             txtOrderCost.setText(String.valueOf(Double.parseDouble(txtOrderCost.getText())+e.getTotal()));
//         });
        double cost = 0;
        for (CartTM tm:tmList
             ) {
            cost+=tm.getTotal();
        }
        lblTotalCost.setText(String.valueOf(cost));
        txtOrderCount.setText(String.valueOf(tmList.size()));
    }
    public void placeOrderOnAction(ActionEvent actionEvent) {
        placeOrder();
    }

    public void addToCartData(ActionEvent actionEvent) {
        addToCartBtn.fire();
    }

    public void newCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUI("CustomerForm","Customer");
    }

    private void setFreshUI(){
        cmdCustomerID.setValue(null);
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        txtOrderCount.setText("Item Count");

        tmList.clear();
        tblCart.refresh();
    }
    private void placeOrder(){
        if(tmList.size()==0){
            new Alert(Alert.AlertType.INFORMATION,"Please select the products with required quantities!").show();
            return;
        }
        // creates item array list [all cart data]
        ArrayList<CartItem> items = new ArrayList<>();
        for (CartTM tm : tmList
        ) {
            items.add(new CartItem(tm.getCode(),tm.getQty(),tm.getUnitPrice()));
        }
        // create order object
        Order order = new Order(txtOrderID.getText(),new Date(),Double.parseDouble(lblTotalCost.getText()),cmdCustomerID.getValue(),items);
        // save order
        Database.orderTable.add(order);
        // update QTY s
        if (manageQty(items)){
            new Alert(Alert.AlertType.INFORMATION,"Order Placed!").show();
            generateOrderID();
            setFreshUI();
        }else {
            // error
        }
    }

    private void generateOrderID() {
        if(!Database.orderTable.isEmpty()){
            Order o = Database.orderTable.get(Database.orderTable.size()-1);
            String id = o.getOrderID();
            //String dataArray[] = id.split("a-z");   // a001    b001
            String dataArray[] = id.split("[a-zA-Z]");   // A001    b001

            id = dataArray[1];
            int oldNum = Integer.parseInt(id);
            oldNum++;

            if (oldNum<9){
                txtOrderID.setText("B00"+oldNum);
            }else if (oldNum<99){
                txtOrderID.setText("B0"+oldNum);
            }else {
                txtOrderID.setText("B"+oldNum);
            }
        }else {
            txtOrderID.setText("B001");
        }
    }

    private boolean manageQty(ArrayList<CartItem> items){
        for (CartItem i : items
             ) {
            Product p = Database.productTable.stream().filter(e -> e.getCode().equals(i.getCode())).findFirst().orElse(null);

            if (p!=null){
                p.setQtyOnHand(p.getQtyOnHand()-i.getQty());
            }else {
                return false;
            }
        }
        return true;
    }
}
