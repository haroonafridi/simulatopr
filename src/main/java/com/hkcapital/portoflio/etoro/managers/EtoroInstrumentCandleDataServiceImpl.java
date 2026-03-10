package com.hkcapital.portoflio.etoro.managers;

import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.ui.InstrumentDataService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class EtoroInstrumentCandleDataServiceImpl implements InstrumentDataService
{

    private static final Logger logger = LoggerFactory.getLogger(EtoroInstrumentCandleDataServiceImpl.class);
    private static final String FORWARD_SLASH = "/";
    private static final String HISTORY_CANDLES = "history/candles/";
    private final EtoroApiConfiguration etoroApiInformation;


    public EtoroInstrumentCandleDataServiceImpl(EtoroApiConfiguration etoroApiInformation)
    {
        this.etoroApiInformation = etoroApiInformation;
    }

    @Override
    public String getInstrumentCandleData(Integer instrument,
                                          String sortOrder,
                                          String timeInterval,
                                          Integer pages)
    {
        logger.info("Requesting candle data: instrument = [{}] timeframe = [{}]  ", instrument, timeInterval);
        try
        {
            final String url = etoroApiInformation.getInstrumentCandleDataUrl().concat(instrument.toString()).concat(FORWARD_SLASH)//
                    .concat(HISTORY_CANDLES).concat(sortOrder).concat(FORWARD_SLASH)
                    .concat(timeInterval).concat(FORWARD_SLASH).concat(pages.toString());
            HttpResponse<String> response = Unirest.get(url)
                    .header(etoroApiInformation.getXRequestId(), UUID.randomUUID().toString())
                    .header(etoroApiInformation.getXApiKey(), etoroApiInformation.getApiKey())
                    .header(etoroApiInformation.getXUserKey(), etoroApiInformation.getUserKey())
                    .asString();
            return response.getBody();
        } catch (UnirestException e)
        {
            logger.info("Error fethcing candle data for interval {}", timeInterval, " instrument {} ", instrument);
            throw new RuntimeException(e);
        }
    }
}
