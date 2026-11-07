package com.hkcapital.portflio.service.orders;

public interface TrendManager
{

    void onTrendChange(TrendIndicatorSignal oldTrend, TrendIndicatorSignal newTrend);
}