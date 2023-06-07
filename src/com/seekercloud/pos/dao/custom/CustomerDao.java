package com.seekercloud.pos.dao.custom;

import com.seekercloud.pos.dao.CrudDao;
import com.seekercloud.pos.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDao extends CrudDao<Customer, String> {
    public ArrayList<Customer> searchCustomer(String searchText) throws SQLException, ClassNotFoundException;
    public ArrayList<Customer> getCustomerDetails(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<String> getCustomerIDs() throws SQLException, ClassNotFoundException;

}
