package com.seekercloud.pos.bo.custom;

import com.seekercloud.pos.dto.OrderDto;
import com.seekercloud.pos.dto.StatisticsDataDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderBo {
    public boolean placeOrder(OrderDto dto, String date) throws SQLException, ClassNotFoundException;
    public ResultSet getQty(String id) throws SQLException, ClassNotFoundException;
    public ResultSet getLastOrderID() throws SQLException, ClassNotFoundException;
    public boolean delete(String id) throws SQLException, ClassNotFoundException;
    public ResultSet loadOrderDetailsTable() throws SQLException, ClassNotFoundException;
    public ResultSet showOrderDetails(String orderId) throws SQLException, ClassNotFoundException;
    public ArrayList<StatisticsDataDto> getDateAndIncome() throws SQLException, ClassNotFoundException;
}
