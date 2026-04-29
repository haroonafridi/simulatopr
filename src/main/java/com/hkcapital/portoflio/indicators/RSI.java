package com.hkcapital.portoflio.indicators;

import java.util.ArrayList;
import java.util.List;

public class RSI {

    private final List<Double> closes = new ArrayList<>();
    private Double avgGain = null;
    private Double avgLoss = null;

    private Double rsi = null;

    public Double onCandleAdd(Candle candle, int period) {
        closes.add(candle.getClose());

        if (closes.size() < period+1) {
            return null; // not enough data
        }

        // --- INITIAL RSI (first calculation) ---
        if (avgGain == null) {
            double gainSum = 0;
            double lossSum = 0;

            for (int i = 1; i <= period; i++) {
                double change = closes.get(i) - closes.get(i - 1);

                if (change > 0) {
                    gainSum += change;
                } else {
                    lossSum += Math.abs(change);
                }
            }

            avgGain = gainSum / period;
            avgLoss = lossSum / period;
        }
        // --- WILDER SMOOTHING ---
        else {
            int last = closes.size() - 1;
            double change = closes.get(last) - closes.get(last - 1);

            double gain = Math.max(change, 0);
            double loss = Math.max(-change, 0);

            avgGain = ((avgGain * (period - 1)) + gain) / period;
            avgLoss = ((avgLoss * (period - 1)) + loss) / period;
        }

        // --- RSI ---
        if (avgLoss == 0) {
            return 100.0;
        }

        double rs = avgGain / avgLoss;
        rsi =  100 - (100 / (1 + rs));
        return rsi;
    }

    public Double getRsi()
    {
        return rsi;
    }
}