package com.seekercloud.pos.bo.custom;

import com.seekercloud.pos.dto.CartItemDto;

import java.sql.SQLException;

public interface CartItemBo {
    public boolean save(CartItemDto dto, String orderID) throws SQLException, ClassNotFoundException;

    public boolean update(CartItemDto dto) throws SQLException, ClassNotFoundException;
}
