package com.hkcapital.portflio.market.structure;

public class PreviousDayMarketRangeDTO {
    private Double low;
    private Double high;

    public PreviousDayMarketRangeDTO(Double low, Double high)
    {
        this.low = low;
        this.high = high;
    }
}