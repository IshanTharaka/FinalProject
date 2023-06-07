package com.seekercloud.pos.dao.custom;

import com.seekercloud.pos.dao.CrudDao;
import com.seekercloud.pos.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDao extends CrudDao<Product,String> {
    public ArrayList<Product> searchProduct(String searchText) throws SQLException, ClassNotFoundException;
    public ArrayList<Product> getProductDetails(String id) throws SQLException, ClassNotFoundException;

    public ArrayList<String> getProductIDs() throws SQLException, ClassNotFoundException;


}
