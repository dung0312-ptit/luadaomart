package com.example.luadaomart.model;

import java.io.Serializable;

public class EmployeeStatistical implements Serializable {
    private String emID;
    private int total;
    private int count;

    public EmployeeStatistical() {
    }

    public EmployeeStatistical(String emID, int total, int count) {
        this.emID = emID;
        this.total = total;
        this.count = count;
    }

    public String getEmID() {
        return emID;
    }

    public void setEmID(String emID) {
        this.emID = emID;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
