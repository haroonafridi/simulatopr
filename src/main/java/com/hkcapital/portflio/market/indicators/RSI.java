package com.hkcapital.portflio.market.indicators;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

import java.util.ArrayList;
import java.util.List;

public class RSI implements IndicatorProcessor
{

    private final int period;
    private final Instrument instrument;

    public RSI(int period, final Instrument instrument)
    {
        this.period = period;
        this.instrument = instrument;
    }

    private final List<Double> closes = new ArrayList<>();
    private Double avgGain = null;
    private Double avgLoss = null;

    private Double rsi = 0d;

    public void onCandleClose(CandleDto candle)
    {

        closes.add(candle.getClose());

        if (closes.size() < period + 1)
        {
            rsi = 0d;
            return;
        }

        // --- INITIAL RSI (first calculation) ---
        if (avgGain == null)
        {
            double gainSum = 0;
            double lossSum = 0;

            for (int i = 1; i <= period; i++)
            {
                double change = closes.get(i) - closes.get(i - 1);

                if (change > 0)
                {
                    gainSum += change;
                } else
                {
                    lossSum += Math.abs(change);
                }
            }

            avgGain = gainSum / period;
            avgLoss = lossSum / period;
        }
        // --- WILDER SMOOTHING ---
        else
        {
            int last = closes.size() - 1;
            double change = closes.get(last) - closes.get(last - 1);

            double gain = Math.max(change, 0);
            double loss = Math.max(-change, 0);

            avgGain = ((avgGain * (period - 1)) + gain) / period;
            avgLoss = ((avgLoss * (period - 1)) + loss) / period;
        }

        // --- RSI ---
        if (avgLoss == 0)
        {
            rsi = 100.0;
        }

        double rs = avgGain / avgLoss;
        rsi = 100 - (100 / (1 + rs));

    }

    @Override
    public String getType()
    {
        return "RSI";
    }

    public Double getRsi()
    {
        return rsi;
    }

}