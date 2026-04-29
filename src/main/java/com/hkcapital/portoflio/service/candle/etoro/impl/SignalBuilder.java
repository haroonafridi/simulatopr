package com.hkcapital.portoflio.service.candle.etoro.impl;

import com.hkcapital.portoflio.indicators.CandleBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignalBuilder
{
    private CandleBuilder candleBuilder1Min;
    private CandleBuilder candleBuilder5Min;
    private CandleBuilder candleBuilder15Min;
    private CandleBuilder candleBuilder30Min;
    private CandleBuilder candleBuilder1Hour;
    private CandleBuilder candleBuilder4Hour;
}
