package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.view.tm.ProductDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailsFormController {
    public AnchorPane orderDetailsContext;
    public TableView<ProductDetailsTM> tblOrders;
    public TableColumn colProductCode;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colCost;
    public JFXTextField txtID;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtSalary;
    public JFXTextField txtOrderID;
    public JFXTextField txtCost;
    public JFXTextField txtDate;

    public void initialize() {
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    public void loadData(String orderID){
        if (orderID==null){
            removeUI();
            return;
        }

        try{
            String sql = "SELECT o.orderId,d.productCode,d.orderID,d.unitPrice,d.qty,o.total,o.customer,o.placeDate" +
                    " FROM `Order` o INNER JOIN `Order Details` d ON o.orderId=d.orderID AND o.orderId=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,orderID);
            ResultSet set = statement.executeQuery();



            ObservableList<ProductDetailsTM> tmList = FXCollections.observableArrayList();

            while (set.next()){
                double tempUnitPrice = set.getDouble(4);
                int tempQtyOnHand = set.getInt(5);
                double tempTotal = tempQtyOnHand * tempUnitPrice;
                ProductDetailsTM tm = new ProductDetailsTM(set.getString(2),
                        set.getDouble(4),
                        set.getInt(5),
                        tempTotal);
                tmList.add(tm);

                String sql1 = "SELECT * FROM Customer WHERE id=?";
                PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                statement1.setString(1, set.getString(7));
                ResultSet resultSet = statement1.executeQuery();

                if (resultSet.next()){
                    txtID.setText(resultSet.getString(1));
                    txtName.setText(resultSet.getString(2));
                    txtAddress.setText(resultSet.getString(3));
                    txtSalary.setText(String.valueOf(resultSet.getDouble(4)));
                }

                txtOrderID.setText(set.getString(1));
                txtDate.setText(set.getString(8));
                txtCost.setText(String.valueOf(set.getDouble(6)));
            }
            tblOrders.setItems(tmList);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void removeUI() {
        Stage stage = (Stage) orderDetailsContext.getScene().getWindow();
        stage.close();
        new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();

    }
}
