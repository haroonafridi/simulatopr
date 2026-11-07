package com.hkcapital.portflio.service.tradingindicators;

import com.hkcapital.portflio.market.indicators.ATRProcessor;
import com.hkcapital.portflio.market.indicators.EMAProcessor;
import com.hkcapital.portflio.market.indicators.RSIProcessor;
import com.hkcapital.portflio.market.indicators.SMAProcessor;
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
                return new SMAProcessor(indicator.getPeriod(), indicator.getInstrument());
            }

            case "EMA" :
            {
                return new EMAProcessor(indicator.getPeriod(), indicator.getInstrument());
            }

            case "ATR" :
            {
                return new ATRProcessor(indicator.getPeriod(), indicator.getInstrument());
            }

            case "RSI" :
            {
                return new RSIProcessor(indicator.getPeriod(), indicator.getInstrument());
            }
            default:
            {
                throw new IllegalArgumentException("No indicator found");
            }
        }

    }
}
