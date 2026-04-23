package com.hkcapital.portoflio.indicators;

public class ATR {

    private final int period;

    private Double prevClose = null;
    private Double atr = null;

    private int trCount = 0;
    private double trSum = 0.0;

    public ATR(int period) {
        this.period = period;
    }

    public Double onCandleAdd(Candle candle) {

        // First candle → no TR possible
        if (prevClose == null) {
            prevClose = candle.getClose();
            return null;
        }

        // --- Step 1: Calculate TR ---
        double high = candle.getHigh();
        double low = candle.getLow();

        double highLow = high - low;
        double highPrevClose = Math.abs(high - prevClose);
        double lowPrevClose = Math.abs(low - prevClose);

        double tr = Math.max(highLow, Math.max(highPrevClose, lowPrevClose));

        // --- Step 2: Build initial ATR (SMA of first N TRs) ---
        if (atr == null) {
            trSum += tr;
            trCount++;

            if (trCount < period) {
                prevClose = candle.getClose();
                return null; // not enough data yet
            }

            // First ATR
            atr = trSum / period;

            prevClose = candle.getClose();
            return atr;
        }

        // --- Step 3: Wilder smoothing (O(1)) ---
        atr = ((atr * (period - 1)) + tr) / period;

        prevClose = candle.getClose();
        return atr;
    }

    public Double getCurrentATR() {
        return atr;
    }

    public boolean isReady() {
        return atr != null;
    }
}