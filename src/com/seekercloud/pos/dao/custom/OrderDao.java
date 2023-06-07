package com.seekercloud.pos.dao.custom;

import com.seekercloud.pos.dao.OrderIDao;
import com.seekercloud.pos.entity.Order;
import com.seekercloud.pos.entity.StatisticsData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDao extends OrderIDao<Order,String> {
    public ResultSet loadOrderDetailsTable() throws SQLException, ClassNotFoundException;
    public ResultSet showOrderDetails(String orderId) throws SQLException, ClassNotFoundException;
    public ArrayList<StatisticsData> getDateAndIncome() throws SQLException, ClassNotFoundException;

}
