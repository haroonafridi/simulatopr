
package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

import java.util.Objects;

public class ATRProcessor implements IndicatorProcessor {

    private final int period;
    private final Instrument instrument;

    private Double prevClose = null;
    private Double atr = null;

    private int trCount = 0;
    private double trSum = 0.0;

    public ATRProcessor(int period, Instrument instrument) {

        Objects.requireNonNull(instrument,
                "Instrument cannot be null");

        if (period <= 0) {
            throw new IllegalArgumentException(
                    "Period must be greater than zero");
        }

        this.period = period;
        this.instrument = instrument;
    }

    @Override
    public void onCandleClose(CandleDto candle) {

        Objects.requireNonNull(candle,
                "Candle cannot be null");

        double high = candle.getHigh();
        double low = candle.getLow();
        double close = candle.getClose();

        // First candle: establish previous close
        if (prevClose == null) {
            prevClose = close;
            return;
        }

        // Calculate True Range
        double tr = Math.max(
                high - low,
                Math.max(
                        Math.abs(high - prevClose),
                        Math.abs(low - prevClose)
                )
        );

        // Initialize ATR using SMA of first N TR values
        if (atr == null) {

            trSum += tr;
            trCount++;

            prevClose = close;

            if (trCount < period) {
                return;
            }

            atr = trSum / period;
            return;
        }

        // Wilder smoothing
        atr = ((atr * (period - 1)) + tr) / period;

        prevClose = close;
    }

    @Override
    public String getType() {
        return "ATR";
    }

    @Override
    public double getValue() {
        return atr == null ? Double.NaN : atr;
    }

    public Double getAtr() {
        return atr;
    }

    public int getPeriod() {
        return period;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public boolean isReady() {
        return atr != null;
    }
}
