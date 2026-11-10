package com.hkcapital.portflio.service.orders;

public class ThirtyMinuteTrend implements TrendManager
{

    @Override
    public void onTrendChange(TrendIndicatorSignal oldTrend, TrendIndicatorSignal newTrend)
    {

        if(newTrend.getEma().getEma9().doubleValue() > oldTrend.getEma().getEma9().doubleValue()) {
            System.out.println("trend change detected");
        }
        // Your business logic here
    }
}