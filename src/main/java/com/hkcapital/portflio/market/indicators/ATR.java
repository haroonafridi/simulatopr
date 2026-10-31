package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

public class ATR implements IndicatorProcessor
{

    private final int period;
    private Double prevClose = null;
    private Double atr = null;

    private int trCount = 0;
    private double trSum = 0.0;

    private final Instrument instrument;

    public ATR(int period, Instrument instrument)
    {
        this.period = period;
        this.instrument = instrument;
    }

    public void onCandleClose(CandleDto candle) {

        // First candle → no TR possible
        if (prevClose == null) {
            prevClose = candle.getClose();
            atr = 0d;
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
            }

            // First ATR
            atr = trSum / period;

            prevClose = candle.getClose();
            return;
        }

        // --- Step 3: Wilder smoothing (O(1)) ---
        atr = ((atr * (period - 1)) + tr) / period;

        prevClose = candle.getClose();
    }

    @Override
    public String getType()
    {
        return "ATR";
    }

    public Double getAtr() {
        return atr;
    }

    public int getPeriod()
    {
        return period;
    }

    public Instrument getInstrument()
    {
        return instrument;
    }

    public boolean isReady() {
        return atr != null;
    }
}