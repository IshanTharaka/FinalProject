package com.seekercloud.pos.dao.custom.impl;

import com.seekercloud.pos.dao.CrudUtil;
import com.seekercloud.pos.dao.custom.OrderDao;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.dto.OrderDto;
import com.seekercloud.pos.entity.Order;
import com.seekercloud.pos.entity.StatisticsData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDaoImpl implements OrderDao {

    @Override
    public boolean placeOrder(OrderDto order, String date) throws SQLException, ClassNotFoundException {
        String sql1 = "INSERT `Order` VALUES(?,?,?,?)";
        return CrudUtil.execute(sql1,order.getOrderID(),date,order.getTotal(),order.getCustomer());
    }


    @Override
    public ResultSet getQty(String id) throws SQLException, ClassNotFoundException {
        String sql1 = "SELECT qtyOnHand FROM Product WHERE code=?";
        return CrudUtil.execute(sql1,id);
    }

    @Override
    public ResultSet getLastOrderID() throws SQLException, ClassNotFoundException {
        String sql1 = "SELECT * FROM `Order` ORDER BY orderId DESC LIMIT 1";   // 10 not working   (UNSIGNED)
        return CrudUtil.execute(sql1);
    }

    @Override
    public ResultSet loadOrderDetailsTable() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM `Order`";
        return CrudUtil.execute(sql);
    }

    @Override
    public ResultSet showOrderDetails(String orderID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT o.orderId,d.productCode,d.orderID,d.unitPrice,d.qty,o.total,o.customer,o.placeDate" +
                " FROM `Order` o INNER JOIN `Order Details` d ON o.orderId=d.orderID AND o.orderId=?";
        return CrudUtil.execute(sql,orderID);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql1 = "DELETE FROM `Order` WHERE orderId=?";
        return CrudUtil.execute(sql1,id);
    }

    @Override
    public ArrayList<StatisticsData> getDateAndIncome() throws SQLException, ClassNotFoundException {

        String sql = "SELECT placeDate, total FROM `Order`";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<StatisticsData> idList = new ArrayList<>();

        while (resultSet.next()){
            idList.add(new StatisticsData(resultSet.getString(1),Double.parseDouble(resultSet.getString(2))));
        }

        return idList;
    }
}
