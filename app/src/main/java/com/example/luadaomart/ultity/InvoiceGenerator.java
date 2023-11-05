package com.example.luadaomart.ultity;

import com.example.luadaomart.model.Order;
import com.example.luadaomart.model.OrderDetail;

import java.util.List;

public class InvoiceGenerator {

    private List<OrderDetail> list;
    private Order order;

    public InvoiceGenerator(List<OrderDetail> list, Order order) {
        this.list = list;
        this.order = order;
    }

    public List<OrderDetail> getList() {
        return list;
    }

    public void setList(List<OrderDetail> list) {
        this.list = list;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void createInvoice (){

    }
}
