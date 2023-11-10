package com.example.luadaomart.model;

public class MonthStatistical {
    private String month;
    private int total;
    private int count;

    public MonthStatistical() {
    }

    public MonthStatistical(String month, int total, int count) {
        this.month = month;
        this.total = total;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
