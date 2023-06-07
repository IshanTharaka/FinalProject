package com.seekercloud.pos.entity;

public class StatisticsData implements SuperEntity{
    private String date;
    private double totalIncome;

    public StatisticsData() {
    }

    public StatisticsData(String date, double totalIncome) {
        this.date = date;
        this.totalIncome = totalIncome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
