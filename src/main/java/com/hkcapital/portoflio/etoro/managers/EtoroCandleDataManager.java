package com.hkcapital.portoflio.etoro.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationService;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationDemoServiceImpl;
import com.hkcapital.portoflio.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.etoro.master.TimeFrame;

import java.text.SimpleDateFormat;

public class EtoroCandleDataManager
{
    public static void main(String args[]) throws JsonProcessingException
    {
        EtoroAPIInformationService apiInformation = new EtoroAPIInformationDemoServiceImpl();

        EtoroInstrumentCandleDataServiceImpl etoroInstrumentCandleData = new EtoroInstrumentCandleDataServiceImpl(apiInformation);

        String intervalData = etoroInstrumentCandleData.getInstrumentCandleData(Instruments.GOLD.getInstrumentId(), "desc",
                TimeFrame.OneDay.name(),
                30);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        mapper.registerModule(new JavaTimeModule());

        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

        CandleResponseDto response =
                mapper.readValue(intervalData, CandleResponseDto.class);

    }
}
