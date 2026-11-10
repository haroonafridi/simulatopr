package com.hkcapital.portflio.service.orders;

public class TrendIndicatorSignal
{
    private String trend;
    private String timeFrameUnit;
    private Integer timeFrame;
    private Double rsi;
    private Double atr;
    private EMA ema;
    private SMA sma;
    private Support support;
    private Resistance resistance;


    public TrendIndicatorSignal(String timeFrameUnit,
                                Integer timeFrame,
                                Double rsi, Double atr,
                                EMA ema, SMA sma,
                                Support support,
                                Resistance resistance)
    {
        this.timeFrameUnit = timeFrameUnit;
        this.timeFrame = timeFrame;
        this.rsi = rsi;
        this.atr = atr;
        this.ema = ema;
        this.sma = sma;
        this.support = support;
        this.resistance = resistance;
    }

    public String getValue()
    {
        return trend;
    }


    public TrendIndicatorSignal copy()
    {
        return new TrendIndicatorSignal(
                timeFrameUnit,
                timeFrame,
                rsi,
                atr,
                ema,
                sma,
                support,
                resistance);
    }

    public static TrendIndicatorSignal of(String timeFrameUnit,
                                   Integer timeFrame,
                                   Double rsi, Double atr,
                                   EMA ema, SMA sma,
                                   Support support,
                                   Resistance resistance)
    {
        return new TrendIndicatorSignal(
                timeFrameUnit,
                timeFrame,
                rsi,
                atr,
                ema,
                sma,
                support,
                resistance);
    }

    public String getTrend()
    {
        return trend;
    }

    public String getTimeFrameUnit()
    {
        return timeFrameUnit;
    }

    public Integer getTimeFrame()
    {
        return timeFrame;
    }

    public Double getRsi()
    {
        return rsi;
    }

    public Double getAtr()
    {
        return atr;
    }

    public EMA getEma()
    {
        return ema;
    }

    public SMA getSma()
    {
        return sma;
    }

    public Support getSupport()
    {
        return support;
    }

    public Resistance getResistance()
    {
        return resistance;
    }

    @Override
    public String toString()
    {
        return "TrendIndicatorSignal{" +
                "trend='" + trend + '\'' +
                ", timeFrameUnit='" + timeFrameUnit + '\'' +
                ", timeFrame=" + timeFrame +
                ", rsi=" + rsi +
                ", atr=" + atr +
                ", ema=" + ema +
                ", sma=" + sma +
                ", support=" + support +
                ", resistance=" + resistance +
                '}';
    }
}
