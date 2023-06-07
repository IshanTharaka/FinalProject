package com.seekercloud.pos.dto;

import com.seekercloud.pos.dto.CartItemDto;

import java.util.ArrayList;
import java.util.Date;

public class OrderDto {
    private String orderID;
    private Date placeDate;
    private double total;
    private String customer;
    private ArrayList<CartItemDto> items;

    public OrderDto() {
    }

    public OrderDto(String orderID, Date placeDate, double total, String customer, ArrayList<CartItemDto> items) {
        this.orderID = orderID;
        this.placeDate = placeDate;
        this.total = total;
        this.customer = customer;
        this.items = items;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Date getPlaceDate() {
        return placeDate;
    }

    public void setPlaceDate(Date placeDate) {
        this.placeDate = placeDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ArrayList<CartItemDto> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItemDto> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderIDao{" +
                "orderID='" + orderID + '\'' +
                ", placeDate=" + placeDate +
                ", total=" + total +
                ", customer='" + customer + '\'' +
                ", items=" + items +
                '}';
    }
}
