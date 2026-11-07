package com.hkcapital.portflio.indicators;

public class ThirtyMinuteTrend implements TrendManager {

    @Override
    public void onTrendChange(Trend oldTrend, Trend newTrend) {

        System.out.println("30 Minute Trend Changed");

        System.out.println("Old: " + oldTrend.getValue());

        System.out.println("New: " + newTrend.getValue());

        // Your business logic here
    }
}