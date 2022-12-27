package com.seekercloud.pos.dao.custom.impl;

import com.seekercloud.pos.dao.CrudUtil;
import com.seekercloud.pos.dao.custom.ProductDao;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDaoImpl implements ProductDao {
    @Override
    public boolean save(Product p) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product VALUES(?,?,?,?)";
//        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
//
//        statement.setString(1,p.getCode());
//        statement.setString(2,p.getDescription());
//        statement.setDouble(3,p.getUnitPrice());
//        statement.setInt(4,p.getQtyOnHand());

        return CrudUtil.execute(sql,p.getCode(),p.getDescription(),p.getUnitPrice(),p.getQtyOnHand());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql1 = "DELETE FROM Product WHERE code=?";
//        PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
//        statement1.setString(1,id);

        return CrudUtil.execute(sql1,id);
    }

    @Override
    public boolean update(Product p) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
//        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
//
//        statement.setString(1,p.getDescription());
//        statement.setDouble(2,p.getUnitPrice());
//        statement.setInt(3,p.getQtyOnHand());
//        statement.setString(4,p.getCode());

       // return statement.executeUpdate()>0;
        return CrudUtil.execute(sql,p.getDescription(),p.getUnitPrice(),p.getQtyOnHand(),p.getCode());
    }

    @Override
    public ArrayList<Product> searchProduct(String searchText) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE description LIKE ?";
//        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        statement.setString(1,searchText);
        ResultSet set = CrudUtil.execute(sql,searchText);

        ArrayList<Product> productList = new ArrayList<>();

        while (set.next()){
            productList.add(new Product(
                    set.getString(1),
                    set.getString(2),
                    set.getDouble(3),
                    set.getInt(4)));
        }
        return productList;
    }
}
