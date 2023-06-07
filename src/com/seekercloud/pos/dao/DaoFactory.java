package com.seekercloud.pos.dao;

import com.seekercloud.pos.dao.custom.CartItemDao;
import com.seekercloud.pos.dao.custom.impl.CartItemDaoImpl;
import com.seekercloud.pos.dao.custom.impl.CustomerDaoImpl;
import com.seekercloud.pos.dao.custom.impl.OrderDaoImpl;
import com.seekercloud.pos.dao.custom.impl.ProductDaoImpl;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return daoFactory==null?(daoFactory= new DaoFactory()):daoFactory;
    }

    public <T> T getDao(DaoTypes types){
        switch (types){
            case CUSTOMER:
                return (T) new CustomerDaoImpl();
            case PRODUCT:
                return (T) new ProductDaoImpl();
            case ORDER:
                return (T) new OrderDaoImpl();
            case CARTITEM:
                return (T) new CartItemDaoImpl();
            default:
                return null;
        }
    }
}
