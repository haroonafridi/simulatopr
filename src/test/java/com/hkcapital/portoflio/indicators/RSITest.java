package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.Test;

import java.time.Instant;

class RSITest
{


    @Test
    public void shouldReturnRSIOf30Min14Period()
    {
        CandleDto candle0 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.41).low(4736.64).open(4736.9).close(4738.41).build();
        CandleDto candle1 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.45).low(4736.79).open(4738.44).close(4736.82).build();
        CandleDto candle2 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.18).low(4736.28).open(4736.83).close(4736.49).build();
        CandleDto candle3 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.53).low(4736.52).open(4736.62).close(4737.2).build();
        CandleDto candle4 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.59).low(4736.03).open(4737.26).close(4736.06).build();
        CandleDto candle5 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4736.36).low(4735.53).open(4735.54).close(4735.65).build();//
        CandleDto candle6 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4735.63).low(4734.08).open(4735.63).close(4734.47).build();//
        CandleDto candle7 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4736.01).low(4733.85).open(4734.45).close(4736.01).build();//
        CandleDto candle8 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.57).low(4735.93).open(4736.14).close(4736.44).build();//
        CandleDto candle9 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4736.95).low(4736.04).open(4736.23).close(4736.22).build();//
        CandleDto candle10 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.47).low(4736.16).open(4736.24).close(4737.07).build();//
        CandleDto candle11 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.65).low(4736.94).open(4737.02).close(4737.55).build();//
        CandleDto candle12 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.8).low(4737.55).open(4737.56).close(4737.8).build();//
        CandleDto candle13 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.99).low(4737.73).open(4737.97).close(4738.6).build();
        CandleDto candle14 = CandleDto.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.98).low(4737.4).open(4738.94).close(4738.1).build();

        RSI rsi = new RSI();
        rsi.onCandleAdd(candle0, 14);
        rsi.onCandleAdd(candle1, 14);
        rsi.onCandleAdd(candle2, 14);
        rsi.onCandleAdd(candle3, 14);
        rsi.onCandleAdd(candle4, 14);
        rsi.onCandleAdd(candle5, 14);
        rsi.onCandleAdd(candle6, 14);
        rsi.onCandleAdd(candle7, 14);
        rsi.onCandleAdd(candle8, 14);
        rsi.onCandleAdd(candle9, 14);
        rsi.onCandleAdd(candle10, 14);
        rsi.onCandleAdd(candle11, 14);
        rsi.onCandleAdd(candle12, 14);
        rsi.onCandleAdd(candle13, 14);
        Double rsiret = rsi.onCandleAdd(candle14, 14);

    }


}