package com.hkcapital.portflio.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrendTopic {

    private Trend trend;

    private final List<TrendManager> managers = new ArrayList<>();

    public TrendTopic(Trend trend) {
        this.trend = trend;
    }

    public void addManager(TrendManager manager) {
        managers.add(manager);
    }

    public void removeManager(TrendManager manager) {
        managers.remove(manager);
    }

    public void updateTrend(Trend newTrend) {

        if (!Objects.equals(trend.getValue(), newTrend.getValue())) {

            Trend oldTrend = trend.copy();

            this.trend = newTrend;

            managers.forEach(manager ->
                    manager.onTrendChange(oldTrend, newTrend)
            );
        }
    }
}