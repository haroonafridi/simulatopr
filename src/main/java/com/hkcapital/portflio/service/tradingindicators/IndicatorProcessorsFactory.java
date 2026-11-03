package com.hkcapital.portflio.service.tradingindicators;

import com.hkcapital.portflio.market.indicators.ATR;
import com.hkcapital.portflio.market.indicators.EMA;
import com.hkcapital.portflio.market.indicators.RSI;
import com.hkcapital.portflio.market.indicators.SMA;
import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.service.orders.IndicatorProcessor;

public class IndicatorProcessorsFactory
{
    public static IndicatorProcessor createProcessor(TradingIndicator indicator)
    {
        switch (indicator.getName())
        {
            case "SMA" :
            {
                return new SMA(indicator.getPeriod(), indicator.getInstrument());
            }

            case "EMA" :
            {
                return new EMA(indicator.getPeriod(), indicator.getInstrument());
            }

            case "ATR" :
            {
                return new ATR(indicator.getPeriod(), indicator.getInstrument());
            }

            case "RSI" :
            {
                return new RSI(indicator.getPeriod(), indicator.getInstrument());
            }
            default:
            {
                throw new IllegalArgumentException("No indicator found");
            }
        }

    }
}
