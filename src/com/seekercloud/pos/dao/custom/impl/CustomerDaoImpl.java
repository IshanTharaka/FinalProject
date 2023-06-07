package com.seekercloud.pos.dao.custom.impl;

import com.seekercloud.pos.dao.CrudUtil;
import com.seekercloud.pos.dao.custom.CustomerDao;
import com.seekercloud.pos.db.DBConnection;
import com.seekercloud.pos.entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(Customer c) throws SQLException, ClassNotFoundException {
        //database code

        // 1 - [driver load ram]
        // Class.forName("com.mysql.cj.jdbc.Driver");

        // 2 - [create connection]
        // Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/pos","root","Ishan@1999");

        // 4 - [create query]
        String sql = "INSERT INTO Customer VALUES(?,?,?,?)";

        // 3 - [create statement]
    //    PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);

        // 5 - [statement execute]
//        statement.setString(1,c.getId());
//        statement.setString(2,c.getName());
//        statement.setString(3,c.getAddress());
//        statement.setDouble(4,c.getSalary());


//                Statement statement = connection.createStatement();
//
//
//                String sql = "INSERT INTO Customer VALUES('"+customer.getId()+"','"+customer.getName()+"','"+customer.getAddress()+"', '"+customer.getSalary()+"' ) ";


        //    int isSaved = statement.executeUpdate(sql);

       // return CrudUtil.executeUpdate(sql,c.getId(),c.getName(),c.getAddress(),c.getSalary());
        return CrudUtil.execute(sql,c.getId(),c.getName(),c.getAddress(),c.getSalary());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql1 = "DELETE FROM Customer WHERE id=?";
//        PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
//        statement1.setString(1,id);

        return CrudUtil.execute(sql1, id);
    }

    @Override
    public boolean update(Customer c) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET name=?, address=?, salary=? WHERE id=?";
//        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
//
//        statement.setString(1,c.getName());
//        statement.setString(2,c.getAddress());
//        statement.setDouble(3,c.getSalary());
//        statement.setString(4,c.getId());

        return CrudUtil.execute(sql, c.getName(),c.getAddress(),c.getSalary(),c.getId());
    }

    @Override
    public ResultSet getLastID() throws SQLException, ClassNotFoundException {
        String sql1 = "SELECT * FROM Customer ORDER BY id DESC LIMIT 1";
        return CrudUtil.execute(sql1);
    }


    @Override
    public ArrayList<Customer> searchCustomer(String searchText) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Customer WHERE name LIKE ? || address LIKE ?";
//        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        statement.setString(1,searchText);
//        statement.setString(2,searchText);
        ResultSet set = CrudUtil.execute(sql,searchText,searchText);

        ArrayList<Customer> list = new ArrayList<>();

        while (set.next()) {
            list.add(new Customer(set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4)));
        }
        return list;
    }

    @Override
    public ArrayList<Customer> getCustomerDetails(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE id=?";
        ResultSet set = CrudUtil.execute(sql,id);

        ArrayList<Customer> list = new ArrayList<>();

        while (set.next()) {
            list.add(new Customer(set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4)));
        }
        return list;
    }

    @Override
    public ArrayList<String> getCustomerIDs() throws SQLException, ClassNotFoundException {
        String sql = "SELECT id FROM Customer";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<String> idList = new ArrayList<>();
        while (resultSet.next()){
            idList.add(resultSet.getString(1));
        }

        return idList;
    }
}
