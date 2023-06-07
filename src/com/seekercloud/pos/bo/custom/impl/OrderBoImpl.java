package com.seekercloud.pos.bo.custom.impl;

import com.seekercloud.pos.bo.custom.OrderBo;
import com.seekercloud.pos.dao.DaoFactory;
import com.seekercloud.pos.dao.DaoTypes;
import com.seekercloud.pos.dao.custom.OrderDao;
import com.seekercloud.pos.dto.CustomerDto;
import com.seekercloud.pos.dto.OrderDto;
import com.seekercloud.pos.dto.StatisticsDataDto;
import com.seekercloud.pos.entity.Customer;
import com.seekercloud.pos.entity.StatisticsData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBoImpl implements OrderBo {
    private OrderDao dao = DaoFactory.getInstance().getDao(DaoTypes.ORDER);
    @Override
    public boolean placeOrder(OrderDto dto, String date) throws SQLException, ClassNotFoundException {
        return dao.placeOrder(dto,date);
    }

    @Override
    public ResultSet getQty(String id) throws SQLException, ClassNotFoundException {
        return dao.getQty(id);
    }

    @Override
    public ResultSet getLastOrderID() throws SQLException, ClassNotFoundException {
        return dao.getLastOrderID();
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return dao.delete(id);
    }

    @Override
    public ResultSet loadOrderDetailsTable() throws SQLException, ClassNotFoundException {
        return dao.loadOrderDetailsTable();
    }

    @Override
    public ResultSet showOrderDetails(String orderId) throws SQLException, ClassNotFoundException {
        return dao.showOrderDetails(orderId);
    }

    @Override
    public ArrayList<StatisticsDataDto> getDateAndIncome() throws SQLException, ClassNotFoundException {
        ArrayList<StatisticsData> entities = dao.getDateAndIncome();
        ArrayList<StatisticsDataDto> dtoList = new ArrayList<>();

        for (StatisticsData s:
                entities) {
            dtoList.add(new StatisticsDataDto(s.getDate(),s.getTotalIncome()));
        }
        return dtoList;
    }
}
