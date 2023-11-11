package com.example.luadaomart.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String id;
    private String name;
    private int price;
    private int amount;
    private int max;
    private int total;

    public OrderDetail() {
    }

    public OrderDetail(String id, String name, int price, int amount, int total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.total = total;
    }

    public OrderDetail(String id, String name, int price, int amount, int max, int total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.max = max;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
