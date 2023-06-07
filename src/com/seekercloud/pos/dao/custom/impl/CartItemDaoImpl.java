package com.seekercloud.pos.dao.custom.impl;

import com.seekercloud.pos.dao.CartItemIDao;
import com.seekercloud.pos.dao.CrudUtil;
import com.seekercloud.pos.dao.custom.CartItemDao;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.entity.CartItem;
import com.seekercloud.pos.entity.SuperEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItemDaoImpl implements CartItemDao {
    @Override
    public boolean save(CartItem cartItem, String orderID) throws SQLException, ClassNotFoundException {
        String sql1 = "INSERT `Order Details` VALUES(?,?,?,?)";
        return CrudUtil.execute(sql1,cartItem.getCode(),orderID,cartItem.getUnitPrice(),cartItem.getQty());
    }

    @Override
    public boolean update(CartItem cartItem) throws SQLException, ClassNotFoundException {
        String sql1 = "UPDATE Product SET qtyOnHand=(qtyOnHand-?) WHERE code=?";
        return CrudUtil.execute(sql1,cartItem.getQty(),cartItem.getCode());
    }


}
