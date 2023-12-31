package com.example.luadaomart.model;

import java.io.Serializable;
import java.util.Map;

public class Order implements Serializable {
    private String id;
    private String employeeId;
    private String employeeName;
    private long timestamps;
    private int totalPrices;
    private String cusPhone;
    private int method;

    public Order() {
    }

    public Order(long timestamps) {
        this.timestamps = timestamps;
    }

    public Order(String employeeId, String employeeName, long timestamps, int totalPrices, String cusPhone, int method) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.timestamps = timestamps;
        this.totalPrices = totalPrices;
        this.cusPhone = cusPhone;
        this.method = method;
    }

    public Order(String id, String employeeId, String employeeName, long timestamps, int totalPrices, String cusPhone, int method) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.timestamps = timestamps;
        this.totalPrices = totalPrices;
        this.cusPhone = cusPhone;
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }

    public int getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(int totalPrices) {
        this.totalPrices = totalPrices;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }
}
