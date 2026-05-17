package com.hkcapital.portflio.market.structure.ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.market.structure.MarketPriceBand;

import java.io.IOException;
import java.io.InputStream;
import java.util.NavigableSet;

public abstract class BandAbstract_UT
{
    public NavigableSet<MarketPriceBand> from(String path) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = getClass().getResourceAsStream(path);

        NavigableSet<MarketPriceBand> marketPriceBands = //
                mapper.readValue(inputStream, new TypeReference<NavigableSet<MarketPriceBand>>()
                {
                });
        return marketPriceBands;
    }
}
