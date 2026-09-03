package com.hkcapital.portflio.service.candle.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.broker.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentCandles;
import com.hkcapital.portflio.repository.candle.CandleRepository;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleResponseMapper;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtoroCandleServiceImpl implements EtoroCandleService
{
    private final CandleRepository candleRepository;
    private final EtoroApiConfiguration apiInformationService;
    private final ObjectMapper objectMapper;
    private final EtoroCandleResponseMapper candleResponseMapper;

    public EtoroCandleServiceImpl(final CandleRepository candleRepository,
                                  final EtoroApiConfiguration apiConfig,
                                  final ObjectMapper objectMapper,
                                  final EtoroCandleResponseMapper candleResponseMapper)
    {
        this.candleRepository = candleRepository;
        this.apiInformationService = apiConfig;
        this.objectMapper = objectMapper;
        this.candleResponseMapper = candleResponseMapper;
    }

    @Override
    public void fetchAndSaveCandleInformation(final Integer instrumentId,
                                              final TimeFrame timeFrame, //
                                              final Integer interval, //
                                              TimeFramesUnit timeTimeFramesUnit)
    {

        CandleResponseDto response = candleResponseMapper.mapResponse(instrumentId, timeFrame, interval);
        List<Candle> candleList = new ArrayList<>();
        for (InstrumentCandles instrumentCandles : response.getCandles())
        {
            instrumentCandles.getCandles().forEach(c ->
            {
                Candle candle = Candle.builder()
                        .creationDateTime(LocalDateTime.now())
                        .instrumentID(c.getInstrumentID())
                        .timeFrame(interval)
                        .timeFrameUnit(timeTimeFramesUnit.getUnit())
                        .open(c.getOpen())
                        .low(c.getLow())
                        .high(c.getHigh())
                        .volume(c.getVolume())
                        .fromDate(Instant.parse(c.getFromDate()))
                        .close(c.getClose())
                        .build();
                candleList.add(candle);
            });
        }
        candleRepository.saveAll(candleList);
    }

    @Override
    public Candle save(Candle candle)
    {
        if(candle.getInstrument() == null) {
            System.out.println("Candle is null!!");
        }
        return candleRepository.save(candle);
    }

    @Override
    public List<Candle> findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(Instrument instrumentID, Integer timeFrame, String timeFrameUnit, LocalDateTime startDate, LocalDateTime endDate)
    {
        return candleRepository.findByInstrumentIDAndTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(instrumentID.getEtoroInstrumentId(),
                timeFrame,
                timeFrameUnit,
                startDate,
                endDate);
    }

    @Override
    public List<Candle> findByTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(Integer timeFrame, String timeFrameUnit, LocalDateTime startDate, LocalDateTime endDate)
    {
        return candleRepository.findByTimeFrameAndTimeFrameUnitAndCreationDateTimeBetween(timeFrame, timeFrameUnit, startDate, endDate);
    }

    @Override
    public List<Candle> findByInstrumentIDAndCreationDateTimeBetween(Integer instrumentID, LocalDateTime startDate, LocalDateTime endDate)
    {
        return candleRepository.findByInstrumentIDAndCreationDateTimeBetween(instrumentID, startDate, endDate);
    }


    @Override
    public List<Candle> findByCreationDateTimeBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    )
    {
        return candleRepository.findByCreationDateTimeBetween(startDate, endDate);
    }


    @Override
    public List<CandleDto> findCandleDtoByInstrumentIDAndCreationDateTimeBetween(Instrument instrument, LocalDateTime startDate, LocalDateTime endDate)
    {

        return candleRepository.findByInstrumentIDAndCreationDateTimeBetween(instrument.getEtoroInstrumentId(), startDate, endDate).stream()
                .map(c -> CandleDto.builder()
                        .instrument(c.getInstrument())
                        .open(c.getOpen())
                        .low(c.getLow())
                        .lowTime(c.getLowTime())
                        .high(c.getHigh())
                        .highTime(c.getHighTime())
                        .close(c.getClose())
                        .interval(c.getTimeFrame())
                        .timeFramesUnit(TimeFramesUnit.valueOf(c.getTimeFrameUnit()))
                        .time(c.getCreationDateTime().atZone(ZoneId.systemDefault()).toInstant())
                        .build())
                .collect(Collectors.toList());
    }


    private String instrument;
    private double open;
    private double low;
    private Instant lowTime;
    private double high;
    private Instant highTime;
    private double close;
    private Instant time;

    private TimeFramesUnit timeFramesUnit;

    private Integer interval;

    @Override
    public void removeAll()
    {
        candleRepository.deleteAll();
    }

}
