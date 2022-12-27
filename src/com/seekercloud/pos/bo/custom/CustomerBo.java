package com.seekercloud.pos.bo.custom;

import com.seekercloud.pos.dto.CustomerDto;
import com.seekercloud.pos.dto.ProductDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBo {

    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<CustomerDto> searchCustomers(String searchText) throws SQLException, ClassNotFoundException;
}
