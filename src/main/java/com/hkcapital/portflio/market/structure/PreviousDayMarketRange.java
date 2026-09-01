package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Instrument;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class PreviousDayMarketRange implements PriceRange
{
    private Instrument instrument;
    private Double low;
    private Double high;
    private Instant date;

    @Override
    public Instrument getInstrument()
    {
        // instrumentTicker has to be added
        return instrument;
    }

    @Override
    public double getLow()
    {
        return low;
    }

    @Override
    public double getHigh()
    {
        return high;
    }

    @Override
    public Instant getDate()
    {
        return date;
    }
}
