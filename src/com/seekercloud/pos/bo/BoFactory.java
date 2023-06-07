package com.seekercloud.pos.bo;

import com.seekercloud.pos.bo.custom.impl.CartItemBoImpl;
import com.seekercloud.pos.bo.custom.impl.CustomerBoImpl;
import com.seekercloud.pos.bo.custom.impl.OrderBoImpl;
import com.seekercloud.pos.bo.custom.impl.ProductBoImpl;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory(){}

    public static BoFactory getInstance(){
        return boFactory==null?(boFactory= new BoFactory()):boFactory;
    }

    public <T> T getBo(BoTypes types){
        switch (types){
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case PRODUCT:
                return (T) new ProductBoImpl();
            case ORDER:
                return (T) new OrderBoImpl();
            case CARTITEM:
                return (T) new CartItemBoImpl();
            default:
                return null;
        }
    }
}
