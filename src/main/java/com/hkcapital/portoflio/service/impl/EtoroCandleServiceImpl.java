package com.hkcapital.portoflio.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationService;
import com.hkcapital.portoflio.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portoflio.etoro.managers.EtoroInstrumentCandleDataServiceImpl;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.etoro.master.TimeFrame;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.InstrumentCandles;
import com.hkcapital.portoflio.repository.CandleRepository;
import com.hkcapital.portoflio.service.EtoroCandleService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EtoroCandleServiceImpl implements EtoroCandleService
{
    private final CandleRepository candleRepository;
    private final EtoroAPIInformationService apiInformationService;

    public EtoroCandleServiceImpl(final CandleRepository candleRepository, final EtoroAPIInformationService apiInformationService)
    {
        this.candleRepository = candleRepository;
        this.apiInformationService = apiInformationService;
    }

    @Override
    public Candle getCandleInformation(TimeFrame timeFrame)
    {
        EtoroInstrumentCandleDataServiceImpl etoroInstrumentCandleData = new EtoroInstrumentCandleDataServiceImpl(apiInformationService);
        String intervalData = etoroInstrumentCandleData.getInstrumentCandleData(Instruments.GOLD.getInstrumentId(), "desc",
                timeFrame.name(),
                30);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            CandleResponseDto response =
                    mapper.readValue(intervalData, CandleResponseDto.class);

            List<Candle> candleList = new ArrayList<>();

            for(InstrumentCandles instrumentCandles : response.getCandles())
            {
                instrumentCandles.getCandles().forEach(c-> {
                    Candle candl =  new Candle();
                    candl.setCreationDateTime(LocalDateTime.now());
                    candl.setTimeFrame(timeFrame.name());
                    candl.setClose(c.getClose());
                    candl.setHigh(c.getHigh());
                    candl.setLow(c.getLow());
                    candl.setVolume(c.getVolume());
                    candl.setInstrumentID(c.getInstrumentID());
                    candl.setVolume(c.getVolume());
                    candl.setFromDate(Instant.parse(c.getFromDate()));
                    candleList.add(candl);
                });
            }

            candleRepository.saveAll(candleList);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Candle save(Candle candle)
    {
        return null;
    }
}
