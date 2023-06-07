package com.seekercloud.pos.dto;

import com.seekercloud.pos.entity.SuperEntity;

public class StatisticsDataDto implements SuperEntity {
    private String date;
    private double totalIncome;

    public StatisticsDataDto() {
    }

    public StatisticsDataDto(String date, double totalIncome) {
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
