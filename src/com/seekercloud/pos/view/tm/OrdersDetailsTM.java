package com.seekercloud.pos.view.tm;

import javafx.scene.control.Button;

public class OrdersDetailsTM {
    private String orderID;
    private String date;
    private double total;
    private String customer;
    private Button btn;

    public OrdersDetailsTM(String orderID, String date, double total, String customer, Button btn) {
        this.orderID = orderID;
        this.date = date;
        this.total = total;
        this.customer = customer;
        this.btn = btn;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "OrdersDetailsTM{" +
                "orderID='" + orderID + '\'' +
                ", date=" + date +
                ", total=" + total +
                ", customerID='" + customer + '\'' +
                ", btn=" + btn +
                '}';
    }
}
