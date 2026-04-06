package com.hkcapital.portoflio.service.candle.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.broker.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portoflio.broker.etoro.master.TimeFrame;
import com.hkcapital.portoflio.service.candle.etoro.EtoroCandleResponseMapper;
import com.hkcapital.portoflio.ui.InstrumentDataService;
import org.springframework.stereotype.Service;

@Service
public class EtoroCandleResponseMapperImpl implements EtoroCandleResponseMapper
{
    private final InstrumentDataService instrumentDataService;
    private final EtoroApiConfiguration apiConfig;
    private final ObjectMapper objectMapper;

    public EtoroCandleResponseMapperImpl(final InstrumentDataService instrumentDataService,
                                         final EtoroApiConfiguration apiConfig,
                                         final ObjectMapper objectMapper)
    {
        this.instrumentDataService = instrumentDataService;
        this.apiConfig = apiConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public CandleResponseDto mapResponse(Integer instrumentId, TimeFrame timeFrame, Integer interval)
    {
        final String candleData = instrumentDataService.getInstrumentCandleData(instrumentId, "desc",
                timeFrame.name(),
                interval);
        try
        {
            return objectMapper.readValue(candleData, CandleResponseDto.class);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
