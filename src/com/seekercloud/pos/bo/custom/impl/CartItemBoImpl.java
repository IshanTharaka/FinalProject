package com.seekercloud.pos.bo.custom.impl;

import com.seekercloud.pos.bo.BoTypes;
import com.seekercloud.pos.bo.custom.CartItemBo;
import com.seekercloud.pos.dao.DaoFactory;
import com.seekercloud.pos.dao.DaoTypes;
import com.seekercloud.pos.dao.custom.CartItemDao;
import com.seekercloud.pos.dto.CartItemDto;
import com.seekercloud.pos.entity.CartItem;

import java.sql.SQLException;

public class CartItemBoImpl implements CartItemBo {
    private CartItemDao dao = DaoFactory.getInstance().getDao(DaoTypes.CARTITEM);
    @Override
    public boolean save(CartItemDto dto, String orderID) throws SQLException, ClassNotFoundException {
        return dao.save(
                new CartItem(dto.getCode(),dto.getQty(),dto.getUnitPrice()),orderID
        );
    }


    @Override
    public boolean update(CartItemDto dto) throws SQLException, ClassNotFoundException {
        return dao.update(
                new CartItem(dto.getCode(),dto.getQty(),dto.getUnitPrice())
        );
    }
}
