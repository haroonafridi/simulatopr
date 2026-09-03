package com.hkcapital.portflio.service.candle.etoro;

import com.hkcapital.portflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.registry.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface EtoroCandleService extends Service
{
    void fetchAndSaveCandleInformation(final Integer instrumentId, final TimeFrame timeFrame, //
                                       final Integer interval, //
                                       TimeFramesUnit timeTimeFramesUnit);

    Candle save(Candle candle);

    //creationDateTime
    List<Candle> findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(
            Instrument instrumentID,
            Integer timeFrame,
            String timeFrameUnit,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Candle> findByCreationDateTimeBetween(
            LocalDateTime startDate,
            LocalDateTime endDate);

    List<Candle> findByTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(
            Integer timeFrame,
            String timeFrameUnit,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Candle> findByInstrumentIDAndCreationDateTimeBetween(
            Integer instrumentID,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<CandleDto> findCandleDtoByInstrumentIDAndCreationDateTimeBetween(
            Instrument instrument,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    void removeAll();
}
