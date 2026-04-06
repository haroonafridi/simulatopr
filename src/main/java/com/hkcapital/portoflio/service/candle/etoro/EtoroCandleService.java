package com.hkcapital.portoflio.service.candle.etoro;

import com.hkcapital.portoflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.service.registry.Service;

import java.time.Instant;
import java.util.List;

public interface EtoroCandleService extends Service
{
    void fetchAndSaveCandleInformation(final Integer instrumentId, final TimeFrame timeFrame, final Integer interval);

    Candle save(Candle candle);

    List<Candle> findCandleByInstrumentIDAndTimeFrameAndFromDateBetween(final Integer instrumentId, final TimeFrame timeFrame, final Instant starDate,
                            final Instant fromDate);
}
