package com.seekercloud.pos.db;

import com.seekercloud.pos.model.Customer;
import com.seekercloud.pos.model.Order;
import com.seekercloud.pos.model.Product;
import com.seekercloud.pos.model.User;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable = new ArrayList<User>();
    public static ArrayList<Customer> customerTable = new ArrayList<Customer>();
    public static ArrayList<Product> productTable = new ArrayList<Product>();
    public static ArrayList<Order> orderTable = new ArrayList<Order>();


    static {
        customerTable.add(new Customer("C-001","Jayantha","Mahara",45000));
        customerTable.add(new Customer("C-002","Kalum","Kadawatha",10000));
        customerTable.add(new Customer("C-003","Nimal","Galle",60000));

        productTable.add(new Product("P-001","Description 1",45,2500));
        productTable.add(new Product("P-002","Description 2",20,156));
        productTable.add(new Product("P-003","Description 3",85,896));
        productTable.add(new Product("P-004","Description 4",12,1569));
        productTable.add(new Product("P-005","Description 5",14,15465));
    }
}
