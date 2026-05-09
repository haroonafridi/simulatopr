package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Instrument;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class PreviousDayMarketRange
{
    private Instrument instrument;
    private Double low;
    private Double high;
    private Instant date;
}
