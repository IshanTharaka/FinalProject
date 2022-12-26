package com.seekercloud.pos.view.tm;

public class ProductDetailsTM {
    private String code;
    private double price;
    private int qty;
    private double total;

    public ProductDetailsTM() {
    }

    public ProductDetailsTM(String code, double price, int qty, double total) {
        this.setCode(code);
        this.setPrice(price);
        this.setQty(qty);
        this.setTotal(total);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProductDetailsTM{" +
                "code='" + code + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", total=" + total +
                '}';
    }
}
