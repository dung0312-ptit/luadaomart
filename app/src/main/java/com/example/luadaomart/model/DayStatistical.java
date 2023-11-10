package com.example.luadaomart.model;

public class DayStatistical {
    private String day;
    private int total;
    private int count;

    public DayStatistical() {
    }

    public DayStatistical(String day, int total, int count) {
        this.day = day;
        this.total = total;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
