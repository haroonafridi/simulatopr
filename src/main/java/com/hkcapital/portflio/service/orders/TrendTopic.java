package com.hkcapital.portflio.service.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrendTopic {

    private TrendIndicatorSignal trend;

    private final List<TrendManager> managers = new ArrayList<>();

    public TrendTopic(TrendIndicatorSignal trend) {
        this.trend = trend;
    }

    public void addManager(TrendManager manager) {
        managers.add(manager);
    }

    public void removeManager(TrendManager manager) {
        managers.remove(manager);
    }

    public void updateTrend(TrendIndicatorSignal newTrend) {

        //if (!Objects.equals(trend.getValue(), newTrend.getValue())) {
            TrendIndicatorSignal oldTrend = trend.copy();
            this.trend = newTrend;
            managers.forEach(manager ->
                    manager.onTrendChange(oldTrend, newTrend)
            );
       // }
    }
}