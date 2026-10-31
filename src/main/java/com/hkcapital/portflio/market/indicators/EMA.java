package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

public class EMA implements IndicatorProcessor
{
    private final Instrument instrument;
    private final double alpha;
    private Double ema = null;

    public EMA(int period, final Instrument instrument)
    {
        this.instrument = instrument;
        this.alpha = 2.0 / (period + 1.0);
    }

    @Override
    public void onCandleClose(CandleDto candle)
    {
        if (ema == null)
        {
            ema = candle.getClose(); // initial seed
        }
        ema = (candle.getClose() * alpha) + (ema * (1 - alpha));
    }

    @Override
    public String getType()
    {
        return "EMA";
    }

    public Double getEma()
    {
        return ema;
    }

    public Instrument getInstrument()
    {
        return instrument;
    }
}