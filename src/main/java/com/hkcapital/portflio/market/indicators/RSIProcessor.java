
package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

import java.util.Objects;

public class RSIProcessor implements IndicatorProcessor {

    private final int period;
    private final Instrument instrument;
    private Double prevClose = null;
    private Double avgGain = null;
    private Double avgLoss = null;
    private double gainSum = 0.0;
    private double lossSum = 0.0;

    private int changeCount = 0;

    private Double rsi = null;

    public RSIProcessor(int period, final Instrument instrument) {

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

        double close = candle.getClose();

        // First candle establishes previous close
        if (prevClose == null) {
            prevClose = close;
            return;
        }

        double change = close - prevClose;

        double gain = Math.max(change, 0.0);
        double loss = Math.max(-change, 0.0);

        prevClose = close;

        // Initial average gain/loss using SMA
        if (avgGain == null) {

            gainSum += gain;
            lossSum += loss;

            changeCount++;

            if (changeCount < period) {
                return;
            }

            avgGain = gainSum / period;
            avgLoss = lossSum / period;

        } else {

            // Wilder smoothing
            avgGain = ((avgGain * (period - 1)) + gain)
                    / period;

            avgLoss = ((avgLoss * (period - 1)) + loss)
                    / period;
        }

        calculateRSI();
    }

    private void calculateRSI() {

        if (avgGain == 0 && avgLoss == 0) {
            rsi = 50.0;
            return;
        }

        if (avgLoss == 0) {
            rsi = 100.0;
            return;
        }

        if (avgGain == 0) {
            rsi = 0.0;
            return;
        }

        double rs = avgGain / avgLoss;

        rsi = 100.0 - (100.0 / (1.0 + rs));
    }

    @Override
    public String getType() {
        return "RSI";
    }

    @Override
    public double getValue() {
        return rsi == null ? Double.NaN : rsi;
    }

    public Double getRsi() {
        return rsi;
    }

    public int getPeriod() {
        return period;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public boolean isReady() {
        return rsi != null;
    }
}
