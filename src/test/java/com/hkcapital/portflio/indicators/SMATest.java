package com.hkcapital.portflio.indicators;

import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.market.indicators.SMA;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

class SMATest extends IndicatorsTestAbstract
{



    @Test
    public void shouldReturn1MinSMA()
    {
        CandleDto candle0 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4738.41).low(4736.64).open(4736.9).close(4738.41).build();
        CandleDto candle1 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4738.45).low(4736.79).open(4738.44).close(4736.82).build();
        CandleDto candle2 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4737.18).low(4736.28).open(4736.83).close(4736.49).build();
        CandleDto candle3 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4737.53).low(4736.52).open(4736.62).close(4737.2).build();
        CandleDto candle4 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4737.59).low(4736.03).open(4737.26).close(4736.06).build();
        CandleDto candle5 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4736.36).low(4735.53).open(4735.54).close(4735.65).build();//
        CandleDto candle6 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4735.63).low(4734.08).open(4735.63).close(4734.47).build();//
        CandleDto candle7 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4736.01).low(4733.85).open(4734.45).close(4736.01).build();//
        CandleDto candle8 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4737.57).low(4735.93).open(4736.14).close(4736.44).build();//
        CandleDto candle9 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4736.95).low(4736.04).open(4736.23).close(4736.22).build();//
        CandleDto candle10 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4737.47).low(4736.16).open(4736.24).close(4737.07).build();//
        CandleDto candle11 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4737.65).low(4736.94).open(4737.02).close(4737.55).build();//
        CandleDto candle12 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4738.8).low(4737.55).open(4737.56).close(4737.8).build();//
        CandleDto candle13 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4738.99).low(4737.73).open(4737.97).close(4738.6).build();
        CandleDto candle14 = CandleDto.builder().time(Instant.now()).timeFramesUnit(TimeFramesUnit.MINUTE).high(4738.98).low(4737.4).open(4738.94).close(4738.1).build();

        SMA sma = new SMA(15, null);
        sma.onCandleClose(candle0);
        sma.onCandleClose(candle1);
        sma.onCandleClose(candle2);
        sma.onCandleClose(candle3);
        sma.onCandleClose(candle4);
        sma.onCandleClose(candle5);
        sma.onCandleClose(candle6);
        sma.onCandleClose(candle7);
        sma.onCandleClose(candle8);
        sma.onCandleClose(candle9);
        sma.onCandleClose(candle10);
        sma.onCandleClose(candle11);
        sma.onCandleClose(candle12);
        sma.onCandleClose(candle13);
        sma.onCandleClose(candle14);
        System.out.println(sma.getSma());
    }

    @Test
    public void testSMATF15Min() throws IOException
    {

        final List<CandleDto> loadCandles = loadCandles("/data/indicators/candles.json");

        SMA sma9 = new SMA(9, null);
        SMA sma20 = new SMA(20, null);

        for (CandleDto e : loadCandles)
        {
            if (e.getInterval().intValue() == 15 && e.getTimeFramesUnit().getUnit().equals("MINUTE"))
            {
              sma9.onCandleClose(e);
              sma20.onCandleClose(e);
            }
        }

        Assertions.assertEquals(4014.413333333332, sma9.getSma(), "SMA 9 does not match");
        Assertions.assertEquals(4016.5164999999993, sma20.getSma(), "SMA 20 does not match");
    }

   @Test
    public void testSMATF30Min() throws IOException
    {

        final List<CandleDto> loadCandles = loadCandles("/data/indicators/candles.json");

        final SMA sma9 = new SMA(9, null);
        final SMA sma20 = new SMA(20, null);
        for (CandleDto e : loadCandles)
        {
            if (e.getInterval().intValue() == 30 && e.getTimeFramesUnit().getUnit().equals("MINUTE"))
            {
                 sma9.onCandleClose(e);
                 sma20.onCandleClose(e);
            }
        }

        Assertions.assertEquals(4016.3222222222216, sma9.getSma(), "SMA 9 does not match");
        Assertions.assertEquals(4015.311999999999, sma20.getSma(), "SMA 20 does not match");
    }



}