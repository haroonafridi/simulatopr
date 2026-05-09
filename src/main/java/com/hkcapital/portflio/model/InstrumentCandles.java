package com.hkcapital.portflio.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hkcapital.portflio.broker.etoro.dto.candle.CandleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentCandles
{
    private int instrumentId;
    private List<CandleDto> candles;
    private double rangeOpen;
    private double rangeClose;
    private double rangeHigh;
    private double rangeLow;
    private double volume;
    private String fromDate;
}
