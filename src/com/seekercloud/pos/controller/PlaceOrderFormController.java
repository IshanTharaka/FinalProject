package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.model.CartItem;
import com.seekercloud.pos.model.Order;
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

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

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

            try{
                String sql = "SELECT * FROM Product WHERE code=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1,cmbProductsCode.getValue());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()){
                    txtDescription.setText(resultSet.getString(2));
                    txtUnitPrice.setText(String.valueOf(resultSet.getDouble(3)));
                    txtQtyOnHand.setText(String.valueOf(resultSet.getInt(4)));
                }

                if(isQtyZero()){
                    new Alert(Alert.AlertType.WARNING,"Quantity on hand is zero. Please select another product!").show();
                    txtQty.setEditable(false);
                }
                txtQty.setEditable(true);

            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
    }

    private boolean isQtyZero(){
        if(Integer.parseInt(txtQtyOnHand.getText())==0){
            txtQty.setVisible(false);
            addToCartBtn.setDisable(true);
            return true;
        }else {
            txtQty.setVisible(true);
            addToCartBtn.setDisable(false);
            txtQty.requestFocus();
            return false;
        }
    }

    private void setCustomerData(String id) {
        try{
            String sql = "SELECT * FROM Customer WHERE id=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,cmdCustomerID.getValue());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                txtName.setText(resultSet.getString(2));
                txtAddress.setText(resultSet.getString(3));
                txtSalary.setText(String.valueOf(resultSet.getDouble(4)));
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void loadCustomerIDs() {
        try{
            String sql2 = "SELECT id FROM Customer";
            PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement(sql2);
            ResultSet resultSet = statement2.executeQuery();

            ArrayList<String> idList = new ArrayList<>();
            while (resultSet.next()){
                idList.add(resultSet.getString(1));
            }
            ObservableList<String> obList = FXCollections.observableArrayList(idList);
            cmdCustomerID.setItems(obList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void loadProductCodes() {

        try{
            String sql1 = "SELECT code FROM Product";
            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
            ResultSet resultSet = statement1.executeQuery();

            ArrayList<String> idList = new ArrayList<>();
            while (resultSet.next()){
                idList.add(resultSet.getString(1));
            }
            ObservableList<String> obList = FXCollections.observableArrayList(idList);
            cmbProductsCode.setItems(obList);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashBoardForm","Dashboard");
    }

    private void setUI(String location,String title) throws IOException {
        Stage window= (Stage) placeOrderContext.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        window.centerOnScreen();
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
            txtQty.requestFocus();
            return;
        }

        Button btn = new Button("Remove");

        CartTM existTM = isExist(cmbProductsCode.getValue());
        CartTM tm = new CartTM(cmbProductsCode.getValue()
                ,txtDescription.getText(),unitPrice,qty,total,btn);

        if (existTM!=null){
            if (!checkQTY(existTM.getQty()+qty)){
                new Alert(Alert.AlertType.WARNING,"Invalid Quantity!").show();
                txtQty.requestFocus();
                return;
            }
            // update
            existTM.setQty(existTM.getQty()+qty);
            existTM.setTotal(existTM.getTotal()+total);
        }else {
            tmList.add(tm);
        }

        btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if(buttonType.get() == ButtonType.YES){
                for (CartTM tempTm : tmList){
                    tmList.remove(tm);
                    tblCart.refresh();
                    setTotalAndCount();
                    return;
                }
            }

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
        try {
            String sql1 = "SELECT qtyOnHand FROM Product WHERE code=?";
            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
            statement1.setString(1,cmbProductsCode.getValue());
            ResultSet set = statement1.executeQuery();

            if(set.next()){
                int tempQty = set.getInt(1);
                if(tempQty>=qty){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
     //   return Integer.parseInt(txtQtyOnHand.getText()) >= qty;
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
    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException {
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
        lblTotalCost.setText("");
    }
    private void placeOrder() throws SQLException {
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

        Connection con = null;

        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            String sql1 = "INSERT `Order` VALUES(?,?,?,?)";
            PreparedStatement statement1 = con.prepareStatement(sql1);
            statement1.setString(1,order.getOrderID());
            statement1.setString(2,txtOrderDate.getText());
            statement1.setDouble(3,order.getTotal());
            statement1.setString(4,order.getCustomer());

            boolean isOrderSaved = statement1.executeUpdate()>0;

            if (isOrderSaved){
                boolean isAllUpdated = manageQty(items);
                if (isAllUpdated){
                    con.commit();
                    new Alert(Alert.AlertType.INFORMATION,"Order Placed!").show();
                    generateOrderID();
                    setFreshUI();
                }
                else {
                    con.setAutoCommit(true);
                    con.rollback();
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }else {
                con.setAutoCommit(true);
                con.rollback();
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            con.setAutoCommit(true);
        }
    }
    private boolean manageQty(ArrayList<CartItem> items){
        try {
            for (CartItem i : items
            ) {
                String sql1 = "INSERT `Order Details` VALUES(?,?,?,?)";
                PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                statement1.setString(1,i.getCode());
                statement1.setString(2,txtOrderID.getText());
                statement1.setDouble(3,i.getUnitPrice());
                statement1.setInt(4,i.getQty());

                boolean isOrderDetailsSaved = statement1.executeUpdate()>0;

                if (isOrderDetailsSaved){
                    boolean isQtyUpdated = update(i);
                    if (!isQtyUpdated){
                        return false;
                    }
                }else {
                    return false;
                }

            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return true;
    }

    private boolean update(CartItem i) {
        try {
            String sql1 = "UPDATE Product SET qtyOnHand=(qtyOnHand-?) WHERE code=?";
            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
            statement1.setInt(1,i.getQty());
            statement1.setString(2,i.getCode());
            return statement1.executeUpdate()>0;
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }
    private void generateOrderID() {

        try {
            String sql1 = "SELECT * FROM `Order` ORDER BY orderId DESC LIMIT 1";   // 10 not working   (UNSIGNED)
            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
            ResultSet set = statement1.executeQuery();

            if (set.next()){
                String id = set.getString(1);
                String dataArray[] = id.split("[a-zA-Z]");   // A001    b001

                id = dataArray[1];
                int oldNum = Integer.parseInt(id);
                oldNum++;

                if (oldNum<=9){
                    txtOrderID.setText("R00"+oldNum);
                }else if (oldNum<=99){
                    txtOrderID.setText("R0"+oldNum);
                }else {
                    txtOrderID.setText("R"+oldNum);
                }
            }else {
                txtOrderID.setText("R001");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
