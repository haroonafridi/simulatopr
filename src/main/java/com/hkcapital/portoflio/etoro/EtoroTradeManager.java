package com.hkcapital.portoflio.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.InstrumentCandles;

import java.util.List;

public class EtoroTradeManager
{
    public static void main(String args[]) throws JsonProcessingException
    {
        EtoroInstrumentCandleData etoroInstrumentCandleData = new EtoroInstrumentCandleData();

        String intervalData = etoroInstrumentCandleData.getInstrumentCandleData(Instruments.GOLD.getInstrumentId(), "desc",
                TimeFrame.OneDay.name(),
                30);

        ObjectMapper mapper = new ObjectMapper();

        CandleResponse response =
                mapper.readValue(intervalData, CandleResponse.class);

//        response.getCandles().forEach(ic ->
//        {
//            int count = 0;
//            for (Candle c : ic.getCandles())
//            {
//               Candle candle = new Candle(c);
//            }
//        });
    }
}
