package com.hkcapital.portflio.market.structure.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hkcapital.portflio.market.structure.MarketStructureJsonWrapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MarketStructureJsonWrapperTest
{
    protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void shouldLoadAndCreateMarketStructure() throws IOException
    {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MarketStructureJsonWrapper marketStructureJsonWrapper =
                objectMapper.readValue(new String(getClass().getResourceAsStream("/data/marketstructure/market-structure.json").readAllBytes(), //
                        StandardCharsets.UTF_8),
                MarketStructureJsonWrapper.class);
        System.out.println(marketStructureJsonWrapper.getMarketStructure());
    }
}
