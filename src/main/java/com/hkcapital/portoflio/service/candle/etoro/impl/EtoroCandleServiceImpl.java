package com.hkcapital.portoflio.service.candle.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.broker.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portoflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.InstrumentCandles;
import com.hkcapital.portoflio.repository.candle.CandleRepository;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleResponseMapper;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public void fetchAndSaveCandleInformation(final Integer instrumentId, final TimeFrame timeFrame, final Integer interval)
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
                        .timeFrame(timeFrame.name())
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
        return candleRepository.save(candle);
    }

    @Override
    public List<Candle> findCandleByInstrumentIDAndTimeFrameAndFromDateBetween(final Integer instrumentId, final TimeFrame timeFrame, final Instant startDate, final Instant endDate)
    {
        return candleRepository.findCandleByInstrumentIDAndTimeFrameAndFromDateBetween(instrumentId, timeFrame.getTimeFrame(), startDate, endDate);
    }
}
