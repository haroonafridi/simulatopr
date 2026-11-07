package com.hkcapital.portflio.service.orders;

public class ThirtyMinuteTrend implements TrendManager {

    @Override
    public void onTrendChange(TrendIndicatorSignal oldTrend, TrendIndicatorSignal newTrend) {

        System.out.println("30 Minute Trend Changed");

        System.out.println("Old: " + oldTrend.getValue());

        System.out.println("New: " + newTrend.getValue());

        // Your business logic here
    }
}