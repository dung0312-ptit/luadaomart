package com.example.luadaomart.model;

public class MonthStatistical {
    private String month;
    private int total;

    public MonthStatistical() {
    }

    public MonthStatistical(String month, int total) {
        this.month = month;
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
