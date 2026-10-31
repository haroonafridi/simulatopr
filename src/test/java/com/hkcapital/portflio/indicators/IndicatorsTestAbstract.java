package com.hkcapital.portflio.indicators;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.market.indicators.CandleDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class IndicatorsTestAbstract
{

    protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public List<CandleDto> loadCandles(String path) throws IOException
    {
        byte[] jsonBytes = loadResource(path).readAllBytes();
        return objectMapper.readValue(jsonBytes, new TypeReference<List<CandleDto>>()
        {
        });
    }

    public InputStream loadResource(String path)
    {
        return getClass().getResourceAsStream(path);
    }
}
