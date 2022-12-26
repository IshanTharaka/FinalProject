package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekercloud.pos.db.Database;
import com.seekercloud.pos.model.CartItem;
import com.seekercloud.pos.model.Customer;
import com.seekercloud.pos.model.Order;
import com.seekercloud.pos.view.tm.ProductDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;

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

        Order o = Database.orderTable.stream().filter(e -> e.getOrderID().equals(orderID))
                .findFirst().orElse(null);

        if (o!=null){
            Customer c = Database.customerTable.stream().filter(e -> e.getId().equals(o.getCustomer()))
                    .findFirst().orElse(null);

            if (c!=null){
                txtID.setText(c.getId());
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtSalary.setText(String.valueOf(c.getSalary()));

                txtOrderID.setText(o.getOrderID());
                txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(o.getPlaceDate()));
                txtCost.setText(String.valueOf(o.getTotal()));

                ObservableList<ProductDetailsTM> tmList = FXCollections.observableArrayList();
                for (CartItem items: o.getItems()
                     ) {
                    tmList.add(new ProductDetailsTM(items.getCode(),
                            items.getUnitPrice(),items.getQty(),(items.getQty()*items.getUnitPrice())));
                }
                tblOrders.setItems(tmList);
            }else {
                removeUI();
            }

        }else {
            removeUI();
        }
    }

    private void removeUI() {
        Stage stage = (Stage) orderDetailsContext.getScene().getWindow();
        stage.close();
        new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();

    }
}
