package com.hkcapital.portoflio.indicators;

public class EMA
{

    private final double alpha;
    private Double ema = null;

    public EMA(int period)
    {
        this.alpha = 2.0 / (period + 1.0);
    }

    public Double onPrice(double price)
    {

        if (ema == null)
        {
            ema = price; // initial seed
            return ema;
        }

        ema = (price * alpha) + (ema * (1 - alpha));
        return ema;
    }

    public Double getEma()
    {
        return ema;
    }
}