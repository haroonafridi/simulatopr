package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class CandlePriceRange implements PriceRange
{

    private final Candle candle;
    public CandlePriceRange(Candle candle)
    {
        this.candle = candle;
    }

    @Override
    public Instrument getInstrument()
    {
        // instrument has to be added
        return null;
    }

    @Override
    public double getLow()
    {
        return candle.getLow();
    }

    @Override
    public double getHigh()
    {
        return candle.getHigh();
    }

    @Override
    public Instant getDate()
    {
        return candle.getFromDate();
    }
}
