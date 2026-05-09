package com.hkcapital.portflio.service.candle.etoro;

import com.hkcapital.portflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portflio.indicators.Unit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.registry.Service;

import java.time.Instant;
import java.util.List;

public interface EtoroCandleService extends Service
{
    void fetchAndSaveCandleInformation(final Integer instrumentId, final TimeFrame timeFrame, //
                                       final Integer interval, //
                                       Unit timeUnit);

    Candle save(Candle candle);

    List<Candle> findCandleByInstrumentIDAndSourceTimeFrameAndTimeFrameUnitFromDateBetween(Integer instrumentID, Integer sourceTimeFrame, //
                                                                                           String sourceTimeFrameUnit, //
                                                                                           Instant startDate, //
                                                                                           Instant endDate);
    void removeAll();
}
