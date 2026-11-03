
package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class SMA implements IndicatorProcessor
{
    private final int period;
    private final Instrument instrument;
    private final Queue<Double> window = new LinkedList<>();
    private double sum = 0.0;

    public SMA(int period, Instrument instrument)
    {
        Objects.requireNonNull(instrument, "Instrument cannot be null");
        if (period <= 0)
        {
            throw new IllegalArgumentException("Period must be greater than zero");
        }
        this.instrument = instrument;
        this.period = period;
    }

    @Override
    public void onCandleClose(CandleDto candle)
    {
        double close = candle.getClose();
        window.add(close);
        sum += close;
        if (window.size() > period)
        {
            sum -= window.poll();
        }
    }
    @Override
    public String getType()
    {
        return "SMA";
    }

    @Override
    public double getValue()
    {
        return getSma();
    }

    public double getSma()
    {
        if (window.size() < period)
        {
            return Double.NaN;
        }

        return sum / period;
    }

    public Instrument getInstrument() {
        return instrument;
    }

}
