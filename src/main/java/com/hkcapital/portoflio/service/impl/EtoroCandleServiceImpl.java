package com.hkcapital.portoflio.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.etoro.CandleResponse;
import com.hkcapital.portoflio.etoro.EtoroInstrumentCandleData;
import com.hkcapital.portoflio.etoro.Instruments;
import com.hkcapital.portoflio.etoro.TimeFrame;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.InstrumentCandles;
import com.hkcapital.portoflio.repository.CandleRepository;
import com.hkcapital.portoflio.service.EtoroCandleService;
import org.springframework.stereotype.Service;

@Service
public class EtoroCandleServiceImpl implements EtoroCandleService
{
    private final CandleRepository candleRepository;

    public EtoroCandleServiceImpl(CandleRepository candleRepository)
    {
        this.candleRepository = candleRepository;
    }

    @Override
    public Candle getCandleInformation(TimeFrame timeFrame)
    {
        EtoroInstrumentCandleData etoroInstrumentCandleData = new EtoroInstrumentCandleData();
        String intervalData = etoroInstrumentCandleData.getInstrumentCandleData(Instruments.GOLD.getInstrumentId(), "desc",
                timeFrame.name(),
                30);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            CandleResponse response =
                    mapper.readValue(intervalData, CandleResponse.class);

            for(InstrumentCandles instrumentCandles : response.getCandles())
            {
                instrumentCandles.getCandles().forEach(c-> {
                c.setTimeFrame(timeFrame.name());
                candleRepository.save(c);
                });

            }
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
