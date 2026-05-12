package com.hkcapital.portflio.service.candle.etoro;

import com.hkcapital.portflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portflio.market.indicators.Unit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.registry.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface EtoroCandleService extends Service
{
    void fetchAndSaveCandleInformation(final Integer instrumentId, final TimeFrame timeFrame, //
                                       final Integer interval, //
                                       Unit timeUnit);

    Candle save(Candle candle);

    //creationDateTime
    List<Candle> findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(
            Integer instrumentID,
            Integer timeFrame,
            String timeFrameUnit,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    void removeAll();
}
