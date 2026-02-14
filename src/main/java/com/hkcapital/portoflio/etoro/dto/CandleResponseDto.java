package com.hkcapital.portoflio.etoro.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hkcapital.portoflio.model.InstrumentCandles;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CandleResponseDto
{

    private String interval;
    private List<InstrumentCandles> candles;

    public CandleResponseDto() {}

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
