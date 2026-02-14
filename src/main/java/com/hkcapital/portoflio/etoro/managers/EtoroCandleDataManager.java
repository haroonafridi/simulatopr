package com.hkcapital.portoflio.etoro.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portoflio.etoro.dto.CandleResponseDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.etoro.master.TimeFrame;

import java.text.SimpleDateFormat;

public class EtoroCandleDataManager
{
    public static void main(String args[]) throws JsonProcessingException
    {
        EtoroInstrumentCandleData etoroInstrumentCandleData = new EtoroInstrumentCandleData();

        String intervalData = etoroInstrumentCandleData.getInstrumentCandleData(Instruments.GOLD.getInstrumentId(), "desc",
                TimeFrame.OneDay.name(),
                30);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        mapper.registerModule(new JavaTimeModule());

        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        CandleResponseDto response =
                mapper.readValue(intervalData, CandleResponseDto.class);

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
