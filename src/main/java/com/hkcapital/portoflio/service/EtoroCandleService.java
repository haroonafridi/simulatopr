package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.etoro.EtoroInstrumentCandleData;
import com.hkcapital.portoflio.etoro.TimeFrame;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Strategy;

import java.util.List;

public interface EtoroCandleService extends Service
{
    Candle getCandleInformation(TimeFrame timeFrame );

    Candle save(Candle candle);
}
