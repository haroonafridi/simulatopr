package com.hkcapital.portoflio.etoro;

import com.hkcapital.portoflio.ui.InstrumentDataManager;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.UUID;

public class EtoroInstrumentCandleData implements InstrumentDataManager
{

    private static final String INSTRUMENT_CANDLE_DATA = "https://public-api.etoro.com/api/v1/market-data/instruments/";


    @Override
    public String getInstrumentCandleData(Integer instrument,
                                          String sortOrder,
                                          String timeInterval,
                                          Integer pages)
    {
        try
        {
            final String url = INSTRUMENT_CANDLE_DATA.concat(instrument.toString()).concat("/")//
                    .concat("history/candles/").concat(sortOrder).concat("/")
                    .concat(timeInterval).concat("/").concat(pages.toString());
            HttpResponse<String> response = Unirest.get(url)
                    .header("x-request-id", UUID.randomUUID().toString())
                    .header("x-api-key", EtoroAPIInformation.API_KEY)
                    .header("x-user-key", EtoroAPIInformation.USER_KEY)
                    .asString();
            return response.getBody();
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }
}
