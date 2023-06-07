package com.seekercloud.pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekercloud.pos.bo.BoFactory;
import com.seekercloud.pos.bo.BoTypes;
import com.seekercloud.pos.bo.custom.CustomerBo;
import com.seekercloud.pos.bo.custom.OrderBo;
import com.seekercloud.pos.dao.DaoFactory;
import com.seekercloud.pos.dao.DaoTypes;
import com.seekercloud.pos.dao.custom.CustomerDao;
import com.seekercloud.pos.dao.custom.OrderDao;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.dto.CustomerDto;
import com.seekercloud.pos.entity.Customer;
import com.seekercloud.pos.view.tm.ProductDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    private CustomerBo customerBo = BoFactory.getInstance().getBo((BoTypes.CUSTOMER));
    private OrderBo orderBo = BoFactory.getInstance().getBo((BoTypes.ORDER));

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
            ResultSet set = orderBo.showOrderDetails(orderID);

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


                ArrayList<CustomerDto> resultSet = customerBo.getCustomerDetails(set.getString(7));

                for (CustomerDto customer:
                        resultSet) {
                    txtID.setText(customer.getId());
                    txtName.setText(customer.getName());
                    txtAddress.setText(customer.getAddress());
                    txtSalary.setText(String.valueOf(customer.getSalary()));
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
