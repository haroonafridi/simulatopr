package com.hkcapital.portoflio.service.candle.etoro;

import com.hkcapital.portoflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.service.registry.Service;

public interface EtoroCandleService extends Service
{
    Candle getCandleInformation(TimeFrame timeFrame );
    Candle save(Candle candle);
}
