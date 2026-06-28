package com.hkcapital.portflio.market.structure.ut;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.it.CandleHelper;
import com.hkcapital.portflio.model.Candle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CandleTest
{
    private static final String PATH = "D:/gold_data/15-06-2026/candle/gold_candle_15_06_2026.csv";
    CandleHelper candleHelper = new CandleHelper(Path.of(PATH));
    @Test
    public void shouldLoadPreviousDayCandles() throws IOException //
    {
        List<Candle> candles1Min =  candleHelper.candleListOf(1, TimeFramesUnit.MINUTE.getUnit());
        List<Candle> candles5Min =  candleHelper.candleListOf(5, TimeFramesUnit.MINUTE.getUnit());
        List<Candle> candles15Min =  candleHelper.candleListOf(15, TimeFramesUnit.MINUTE.getUnit());
        List<Candle> candles30Min =  candleHelper.candleListOf(30, TimeFramesUnit.MINUTE.getUnit());
        List<Candle> candles1Hour =  candleHelper.candleListOf(1, TimeFramesUnit.HOUR.getUnit());
        List<Candle> candles4Hour =  candleHelper.candleListOf(4, TimeFramesUnit.HOUR.getUnit());
        Assertions.assertEquals(1358, candles1Min.size());
        Assertions.assertEquals(275, candles5Min.size());
        Assertions.assertEquals(92, candles15Min.size());
        Assertions.assertEquals(46, candles30Min.size());
        Assertions.assertEquals(23, candles1Hour.size());
        Assertions.assertEquals(7, candles4Hour.size());
    }
}
