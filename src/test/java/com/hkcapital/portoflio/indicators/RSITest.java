package com.hkcapital.portoflio.indicators;

import org.junit.jupiter.api.Test;

import java.time.Instant;

class RSITest
{


    @Test
    public void shouldReturnRSIOf30Min14Period()
    {
        Candle candle0 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.41).low(4736.64).open(4736.9).close(4738.41).build();
        Candle candle1 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.45).low(4736.79).open(4738.44).close(4736.82).build();
        Candle candle2 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.18).low(4736.28).open(4736.83).close(4736.49).build();
        Candle candle3 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.53).low(4736.52).open(4736.62).close(4737.2).build();
        Candle candle4 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.59).low(4736.03).open(4737.26).close(4736.06).build();
        Candle candle5 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4736.36).low(4735.53).open(4735.54).close(4735.65).build();//
        Candle candle6 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4735.63).low(4734.08).open(4735.63).close(4734.47).build();//
        Candle candle7 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4736.01).low(4733.85).open(4734.45).close(4736.01).build();//
        Candle candle8 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.57).low(4735.93).open(4736.14).close(4736.44).build();//
        Candle candle9 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4736.95).low(4736.04).open(4736.23).close(4736.22).build();//
        Candle candle10 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.47).low(4736.16).open(4736.24).close(4737.07).build();//
        Candle candle11 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4737.65).low(4736.94).open(4737.02).close(4737.55).build();//
        Candle candle12 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.8).low(4737.55).open(4737.56).close(4737.8).build();//
        Candle candle13 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.99).low(4737.73).open(4737.97).close(4738.6).build();
        Candle candle14 = Candle.builder().time(Instant.now()).unit(Unit.MINUTE).high(4738.98).low(4737.4).open(4738.94).close(4738.1).build();

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
        System.out.println(rsiret);

    }


}