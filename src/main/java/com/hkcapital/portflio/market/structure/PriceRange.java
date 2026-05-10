package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Instrument;

import java.time.Instant;

public interface PriceRange
{

    Instrument getInstrument();

    double getLow();

    double getHigh();

    Instant getDate();
}
