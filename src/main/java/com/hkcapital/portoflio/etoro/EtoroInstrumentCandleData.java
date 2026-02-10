package com.hkcapital.portoflio.etoro;

import com.hkcapital.portoflio.ui.InstrumentDataManager;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.UUID;

import static com.hkcapital.portoflio.etoro.EtoroAPIInformation.*;
import static com.hkcapital.portoflio.etoro.EtoroAPIInformation.INSTRUMENT_CANDLE_DATA;

public class EtoroInstrumentCandleData implements InstrumentDataManager
{

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
                    .header(X_REQUEST_ID, UUID.randomUUID().toString())
                    .header(X_API_KEY, API_KEY)
                    .header(X_USER_KEY, USER_KEY)
                    .asString();
            return response.getBody();
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }
}
