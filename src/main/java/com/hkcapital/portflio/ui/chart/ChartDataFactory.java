package com.hkcapital.portflio.ui.chart;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.values.timeframe.TimeFrame;

public class ChartDataFactory
{
    public ChartDataGenerator createGenerator(TimeFramesUnit timeFramesUnit,
                                              TimeFrame timeFrame,
                                              Instrument instrument)
    {
        if (TimeFramesUnit.MINUTE.equals(timeFramesUnit) && 5 == timeFrame.timeFrame().intValue())
        {
        }

        return null;
    }
}
