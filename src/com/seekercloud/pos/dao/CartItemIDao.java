package com.seekercloud.pos.dao;

import com.seekercloud.pos.entity.SuperEntity;

import java.sql.SQLException;

public interface CartItemIDao <T extends SuperEntity> {
    public boolean save(T t, String orderID) throws SQLException, ClassNotFoundException;

    public boolean update(T t) throws SQLException, ClassNotFoundException;

}
