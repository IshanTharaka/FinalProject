package com.seekercloud.pos.bo.custom;

import com.seekercloud.pos.dto.ProductDto;
import com.seekercloud.pos.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductBo {

    public boolean saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException;
    public boolean updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException;
    public boolean deleteProduct(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<ProductDto> searchProducts(String searchText) throws SQLException, ClassNotFoundException;
    public ResultSet getLastID() throws SQLException, ClassNotFoundException;
    public ArrayList<Product> getProductDetails(String id) throws SQLException, ClassNotFoundException;

    public ArrayList<String> getProductIDs() throws SQLException, ClassNotFoundException;

}
