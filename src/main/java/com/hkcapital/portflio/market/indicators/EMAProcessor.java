
package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

import java.util.Objects;

public class EMAProcessor implements IndicatorProcessor {

    private final Instrument instrument;
    private final int period;
    private final double alpha;

    private Double ema = null;
    private double seedSum = 0.0;
    private int seedCount = 0;

    public EMAProcessor(int period, final Instrument instrument) {

        Objects.requireNonNull(instrument,
                "Instrument cannot be null");

        if (period <= 0) {
            throw new IllegalArgumentException(
                    "Period must be greater than zero");
        }

        this.instrument = instrument;
        this.period = period;
        this.alpha = 2.0 / (period + 1.0);
    }

    @Override
    public void onCandleClose(CandleDto candle) {

        double close = candle.getClose();

        // Initialize EMA using SMA of first N candles
        if (ema == null) {
            seedSum += close;
            seedCount++;

            if (seedCount == period) {
                ema = seedSum / period;
            }
            return;
        }
        // Standard recursive EMA calculation
        ema = (close * alpha)
                + (ema * (1.0 - alpha));
    }

    @Override
    public String getType() {
        return "EMA";
    }

    @Override
    public double getValue() {
        return ema == null ? Double.NaN : ema;
    }

    @Override
    public int getPeriod()
    {
        return period;
    }

    public Double getEma() {
        return ema;
    }

    public Instrument getInstrument() {
        return instrument;
    }
}
