package com.hkcapital.portflio.indicators;

public interface TrendManager {

    void onTrendChange(Trend oldTrend, Trend newTrend);
}