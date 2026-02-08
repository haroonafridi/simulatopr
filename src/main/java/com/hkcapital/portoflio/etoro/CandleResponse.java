package com.hkcapital.portoflio.etoro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hkcapital.portoflio.model.InstrumentCandles;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CandleResponse {

    private String interval;
    private List<InstrumentCandles> candles;

    public CandleResponse() {}

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public List<InstrumentCandles> getCandles() {
        return candles;
    }

    public void setCandles(List<InstrumentCandles> candles) {
        this.candles = candles;
    }
}
