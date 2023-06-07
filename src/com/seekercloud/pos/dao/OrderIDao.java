package com.seekercloud.pos.dao;

import com.seekercloud.pos.dto.OrderDto;
import com.seekercloud.pos.entity.SuperEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderIDao<T extends SuperEntity,ID> {

    public boolean placeOrder(OrderDto t, String date) throws SQLException, ClassNotFoundException;
    public ResultSet getQty(String id) throws SQLException, ClassNotFoundException;
    public ResultSet getLastOrderID() throws SQLException, ClassNotFoundException;
    public boolean delete(String id) throws SQLException, ClassNotFoundException;

}
