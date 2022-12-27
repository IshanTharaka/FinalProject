package com.seekercloud.pos.bo;

import com.seekercloud.pos.bo.custom.impl.CustomerBoImpl;
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
            default:
                return null;
        }
    }
}
