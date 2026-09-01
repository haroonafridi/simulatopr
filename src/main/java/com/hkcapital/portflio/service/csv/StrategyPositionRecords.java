package com.hkcapital.portflio.service.csv;

public record StrategyPositionRecords(
        String strategyName,
        String strategyDesc,
        double capitalAllocated,
        boolean strategyActive,
        String instrumentTicker,
        String instrumentName,
        String instrumentDesc,
        String url,
        int etoroId,
        int timeFrame,
        String timeFrameUnit,
        double slippage,
        double amount,
        int lev,
        String positionType,
        double lSupport,
        double support,
        double rSupport,
        double lResistance,
        double resistance,
        double rResistance,
        double takeProfit,
        double stopLoss,
        int executionCount,
        boolean active,
        boolean withFeed,
        boolean withBand,
        boolean withCandle
)
{
}
