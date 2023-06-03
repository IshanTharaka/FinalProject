package com.seekercloud.pos.bo.custom.impl;

import com.seekercloud.pos.bo.custom.CustomerBo;
import com.seekercloud.pos.dao.DaoFactory;
import com.seekercloud.pos.dao.DaoTypes;
import com.seekercloud.pos.dao.custom.CustomerDao;
import com.seekercloud.pos.dto.CustomerDto;
import com.seekercloud.pos.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBoImpl implements CustomerBo {

    private CustomerDao dao = DaoFactory.getInstance().getDao(DaoTypes.CUSTOMER);
    @Override
    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return dao.save(
                new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary())
        );
    }

    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return dao.update(
                new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary())
        );
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return dao.delete(id);
    }

    @Override
    public ArrayList<CustomerDto> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> entities = dao.searchCustomer(searchText);
        ArrayList<CustomerDto> dtoList = new ArrayList<>();

        for (Customer c:
             entities) {
            dtoList.add(new CustomerDto(c.getId(),c.getName(),c.getAddress(),c.getSalary()));
        }
        return dtoList;
    }
}
