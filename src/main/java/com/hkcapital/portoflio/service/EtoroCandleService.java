package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.etoro.master.TimeFrame;
import com.hkcapital.portoflio.model.Candle;

public interface EtoroCandleService extends Service
{
    Candle getCandleInformation(TimeFrame timeFrame );

    Candle save(Candle candle);
}
